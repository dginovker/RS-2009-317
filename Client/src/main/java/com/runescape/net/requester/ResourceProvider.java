package com.runescape.net.requester;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.collection.Deque;
import com.runescape.collection.Queue;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

public final class ResourceProvider extends Provider implements Runnable {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(ResourceProvider.class.getName());
    private final int[][] crcs;
    private final CRC32 crc32;
    private final Deque requested;
    private final byte[] payload;
    private final byte[][] fileStatus;
    private final Deque extras;
    private final Deque complete;
    private final byte[] gzipInputBuffer;
    private final Queue requests;
    private final int[][] versions;
    private final Deque unrequested;
    private final Deque mandatoryRequests;
    public String loadingMessage;
    public int tick;
    public int errors;
    public Resource current;
    private int totalFiles;
    private int maximumPriority;
    private int deadTime;
    private long lastRequestTime;
    private int[] landscapes;
    private Game clientInstance;
    private int completedSize;
    private int remainingData;
    private int[] musicPriorities;
    private int[] mapFiles;
    private int filesLoaded;
    private boolean running;
    private OutputStream outputStream;
    private int[] membersArea;
    private boolean expectingData;
    private InputStream inputStream;
    private Socket socket;
    private int uncompletedCount;
    private int completedCount;
    private int[] areas;
    private byte[] modelIndices;
    private int idleTime;

    public ResourceProvider() {
        requested = new Deque();
        loadingMessage = "";
        crc32 = new CRC32();
        payload = new byte[500];
        fileStatus = new byte[Constants.CACHE_INDICES - 1][];
        extras = new Deque();
        running = true;
        expectingData = false;
        complete = new Deque();
        gzipInputBuffer = new byte[0x71868];
        requests = new Queue();
        versions = new int[Constants.CACHE_INDICES - 1][];
        unrequested = new Deque();
        mandatoryRequests = new Deque();
        crcs = new int[Constants.CACHE_INDICES - 1][];
    }

    private void respond() {
        try {
            int readAbleBytes = inputStream.available();
            if (remainingData == 0 && readAbleBytes >= 10) {
                expectingData = true;
                for (int k = 0; k < 10; k += inputStream.read(payload, k, 10 - k)) {
                    ;
                }
                int receivedType = payload[0] & 0xff;
                int receivedID = ((payload[1] & 0xff) << 16) + ((payload[2] & 0xff) << 8) + (payload[3] & 0xff);
                int receivedSize = ((payload[4] & 0xff) << 32) + ((payload[5] & 0xff) << 16) + ((payload[6] & 0xff) << 8) + (payload[7] & 0xff);
                int chunkId = ((payload[8] & 0xff) << 8) + (payload[9] & 0xff);
                current = null;
                for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null;
                     resource = (Resource) requested.reverseGetNext()) {
                    if (resource.dataType == receivedType && resource.ID == receivedID) {
                        current = resource;
                    }
                    if (current != null) {
                        resource.loopCycle = 0;
                    }
                }
                if (current != null) {
                    idleTime = 0;
                    if (receivedSize == 0) {
                        current.buffer = null;
                        if (current.incomplete) {
                            synchronized (complete) {
                                complete.insertHead(current);
                            }
                        } else {
                            current.unlink();
                        }
                        current = null;
                    } else {
                        if (current.buffer == null && chunkId == 0) {
                            current.buffer = new byte[receivedSize];
                        }
                        if (current.buffer == null && chunkId != 0) {
                            throw new IOException("missing start of file");
                        }
                    }
                }
                completedSize = chunkId * 500;
                remainingData = 500;
                if (remainingData > receivedSize - chunkId * 500) {
                    remainingData = receivedSize - chunkId * 500;
                }
            }
            if (remainingData > 0 && readAbleBytes >= remainingData) {
                expectingData = true;
                byte receivedData[] = payload;
                int tempCompSize = 0;
                if (current != null) {
                    receivedData = current.buffer;
                    tempCompSize = completedSize;
                }
                for (int dataReadIdx = 0; dataReadIdx < remainingData; dataReadIdx += inputStream.read(receivedData, dataReadIdx + tempCompSize, remainingData - dataReadIdx)) {
                    ;
                }
                if (remainingData + completedSize >= receivedData.length && current != null) {
                    //if(clientInstance.indices[0] != null)
                    clientInstance.indices[current.dataType + 1].put(receivedData.length, receivedData, current.ID);
                    if (!current.incomplete && current.dataType == 3 || current.dataType == 7) {
                        current.incomplete = true;
                        current.dataType = 93;
                    }
                    if (current.incomplete) {
                        synchronized (complete) {
                            complete.insertHead(current);
                        }
                    } else {
                        current.unlink();
                    }
                }
                remainingData = 0;
                return;
            }
        } catch (IOException ioexception) {
            try {
                socket.close();
            } catch (Exception _ex) {
            }
            socket = null;
            inputStream = null;
            outputStream = null;
            remainingData = 0;
        }
    }

    public void initialize(CacheArchive cacheArchive, Game game) {
        if (!Constants.DUMPING_CRCS) {
            for (int cachePtr = 0; cachePtr < Constants.CACHE_INDICES - 3; cachePtr++) {//TODO - 1
          /*      byte[] crcFile = cacheArchive.getEntry(cachePtr + "_crc.dat");
                int length = 0;
                length = crcFile.length / 4;
                RSStream crcStream = new RSStream(crcFile);
                crcs[cachePtr] = new int[length];*/
                fileStatus[cachePtr] = new byte[50000 + 100];
                if (cachePtr == 6) {
                    //System.out.println(fileStatus[cachePtr].length);
                    for (int i = 0; i < fileStatus[cachePtr].length; i++) {
                        fileStatus[cachePtr][i] = 0;
                    }
                }
               /* for (int ptr = 0; ptr < length; ptr++) {
                    crcs[cachePtr][ptr] = crcStream.getInt();
                }*/
            }
        }


        byte[] osrsModelIndex = (FileUtility.getBytes(Constants.getCachePath(true) + "/config/map_index"));
        RSStream osrsStream = new RSStream(osrsModelIndex);
        int fileCountOSRS = osrsStream.getShort();

        byte[] modelIndex;
        RSStream rsStream;
        int fileCount = 0;
        areas = new int[fileCountOSRS];
        mapFiles = new int[fileCountOSRS];
        landscapes = new int[ fileCountOSRS];

        for (int index = 0; index < fileCountOSRS; index++) {
            areas[index] = osrsStream.getShort();
            mapFiles[index] = osrsStream.getShort();
            landscapes[index] = osrsStream.getShort();
        }

        //TODO Repack map_index
        areas[107] = 8751;  //region id?
        mapFiles[107] = 1946; //map file id
        landscapes[107] = 1947; //landscape file id k ezpz
        areas[108] = 8752;
        mapFiles[108] = 938;
        landscapes[108] = 939;
        areas[129] = 9007;
        mapFiles[129] = 1938;
        landscapes[129] = 1939;
        areas[130] = 9008;
        mapFiles[130] = 946;
        landscapes[130] = 947;
        areas[149] = 9263;
        mapFiles[149] = 1210;
        landscapes[149] = 1211;
        areas[150] = 9264;
        mapFiles[150] = 956;
        landscapes[150] = 957;
        if (Constants.JAGGRAB_ENABLED) {
            modelIndex = cacheArchive.getEntry("model_index");
            int length = crcs[0].length;
            modelIndices = new byte[length];
            for (int k1 = 0; k1 < length; k1++) {
                if (k1 < modelIndex.length) {
                    modelIndices[k1] = modelIndex[k1];
                } else {
                    modelIndices[k1] = 0;
                }
            }
        }

        modelIndex = cacheArchive.getEntry("midi_index");
        rsStream = new RSStream(modelIndex);
        fileCount = modelIndex.length;
        musicPriorities = new int[fileCount];
        for (int k2 = 0; k2 < fileCount; k2++) {
            musicPriorities[k2] = rsStream.getByte();
        }
        clientInstance = game;
        running = true;
        clientInstance.startRunnable(this, 2);
        if (Constants.DUMPING_CRCS) {
            writeAll();
        }
    }

    public int remaining() {
        synchronized (requests) {
            return requests.size();
        }
    }

    public void disable() {
        running = false;
    }

    public void preloadMaps(boolean members) {
        for (int area = 0; area < areas.length; area++) {
            if (members || membersArea[area] != 0) {
                requestExtra((byte) 2, 7, landscapes[area]);
                requestExtra((byte) 2, 7, mapFiles[area]);
            }
        }
    }

    public int getVersionCount(int index) {
        return versions[index].length;
    }

    private void request(Resource resource) {
        try {
            if (socket == null) {
                long l = System.currentTimeMillis();
                if (l - lastRequestTime < 4000L) {
                    return;
                }
                lastRequestTime = l;
                socket = clientInstance.openSocket(Constants.JAGGRAB_HOST, Constants.JAGGRAB_SERVICE_PORT);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(15);
                for (int j = 0; j < 8; j++) {
                    inputStream.read();
                }
                idleTime = 0;
            }
            payload[0] = (byte) resource.dataType;
            payload[1] = (byte) (resource.ID >> 24);
            payload[2] = (byte) (resource.ID >> 16);
            payload[3] = (byte) (resource.ID >> 8);
            payload[4] = (byte) resource.ID;
            if (resource.incomplete) {
                payload[5] = 1;
            } else if (!Game.loggedIn) {
                payload[5] = 1;
            } else {
                payload[5] = 0;
            }
            outputStream.write(payload, 0, 6);
            deadTime = 0;
            errors = -10000;
            return;
        } catch (IOException ioexception) {
        }
        try {
            socket.close();
        } catch (Exception _ex) {
        }
        socket = null;
        inputStream = null;
        outputStream = null;
        remainingData = 0;
    }

    public int getModelCount() {
        return 45522;
    }

    @Override
    public void provide(int type, int file) {
        synchronized (requests) {
            for (Resource resource = (Resource) requests.reverseGetFirst(); resource != null; resource = (Resource) requests.reverseGetNext()) {
                if (resource.dataType == type && resource.ID == file) {
                    return;
                }
            }
            Resource resource = new Resource();
            resource.dataType = type;
            resource.ID = file;
            resource.incomplete = true;
            synchronized (mandatoryRequests) {
                mandatoryRequests.insertHead(resource);
            }
            requests.insertHead(resource);
        }
    }

    public int getModelIndex(int modelId) {
        if (modelIndices == null) {
            return 0;
        }
        return modelIndices[modelId] & 0xff;
    }

    public void run() {
        try {
            while (running) {
                tick++;
                int sleepTime = 20;
                if (maximumPriority == 0 && clientInstance.indices[0] != null) {
                    sleepTime = 50;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                expectingData = true;
                for (int index = 0; index < 100; index++) {
                    if (!expectingData) {
                        break;
                    }
                    expectingData = false;
                    loadMandatory();
                    requestMandatory();
                    if (uncompletedCount == 0 && index >= 5) {
                        break;
                    }
                    loadExtra();
                    if (inputStream != null) {
                        respond();
                    }
                }

                boolean idle = false;
                for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null; resource = (Resource) requested.reverseGetNext()) {
                    if (resource.incomplete) {
                        idle = true;
                        resource.loopCycle++;
                        if (resource.loopCycle > 50) {
                            resource.loopCycle = 0;
                            request(resource);
                        }
                    }
                }

                if (!idle) {
                    for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null; resource = (Resource) requested.reverseGetNext()) {
                        idle = true;
                        resource.loopCycle++;
                        if (resource.loopCycle > 50) {
                            resource.loopCycle = 0;
                            request(resource);
                        }
                    }

                }
                if (idle) {
                    idleTime++;
                    if (idleTime > 750) {
                        try {
                            socket.close();
                        } catch (Exception _ex) {
                        }
                        socket = null;
                        inputStream = null;
                        outputStream = null;
                        remainingData = 0;
                    }
                } else {
                    idleTime = 0;
                    loadingMessage = "";
                }
                if (Game.loggedIn && socket != null && outputStream != null && (maximumPriority > 0 || clientInstance.indices[0] == null)) {
                    deadTime++;
                    if (deadTime > 500) {
                        deadTime = 0;
                        payload[0] = 0;
                        payload[1] = 0;
                        payload[2] = 0;
                        payload[3] = 10;
                        try {
                            outputStream.write(payload, 0, 4);
                        } catch (IOException _ex) {
                            idleTime = 5000;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void loadExtra(int type, int file) {
        if (clientInstance.indices[0] == null) {
            return;
        }
        if (maximumPriority == 0) {
            return;
        }
        Resource resource = new Resource();
        resource.dataType = file;
        resource.ID = type;
        resource.incomplete = false;
        synchronized (extras) {
            extras.insertHead(resource);
        }
    }

    public Resource next() {
        Resource resource;
        synchronized (complete) {
            resource = (Resource) complete.popHead();
        }
        if (resource == null) {
            return null;
        }
        synchronized (requests) {
            resource.unlinkCacheable();
        }
        if (resource.buffer == null) {
            return resource;
        }
        int read = 0;
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(resource.buffer));
            do {
                if (read == gzipInputBuffer.length) {
                    throw new RuntimeException("buffer overflow!");
                }
                int in = gis.read(gzipInputBuffer, read, gzipInputBuffer.length - read);
                if (in == -1) {
                    break;
                }
                read += in;
            } while (true);
        } catch (IOException _ex) {
            if (Constants.DEBUG_MODE) {
                logger.log(Level.WARNING, "Failed to unzip data, type: {0}, id: {1}.", new Object[]{resource.dataType, resource.ID});
            }
            _ex.printStackTrace();
            return null;
        }
        resource.buffer = new byte[read];
        System.arraycopy(gzipInputBuffer, 0, resource.buffer, 0, read);

        return resource;
    }

    public int resolve(int type, int regionX, int regionY) {
        int regionId = (regionY << 8) + regionX;
        for (int area = 0; area < areas.length; area++) {
            if (areas[area] == regionId) {
                if (type == 0) {
                    return mapFiles[area];
                } else {
                    return landscapes[area];
                }
            }
        }
        return -1;
    }

    public void requestExtra(byte priority, int index, int fileId) {
        if (clientInstance.indices[0] == null) {
            return;
        }
        byte data[] = clientInstance.indices[index + 1].decompress(fileId);
        if (crcMatches(crcs[index][fileId], data)) {
            return;
        }
        fileStatus[index][fileId] = priority;
        if (priority > maximumPriority) {
            maximumPriority = priority;
        }
        totalFiles++;
    }

    public boolean landscapePresent(int landscape) {
        for (int index = 0; index < areas.length; index++) {
            if (landscapes[index] == landscape) {
                return true;
            }
        }
        return false;
    }

    private void requestMandatory() {
        uncompletedCount = 0;
        completedCount = 0;
        for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null;
             resource = (Resource) requested.reverseGetNext()) {
            if (resource.incomplete) {
                uncompletedCount++;
            } else {
                completedCount++;
            }
        }

        while (uncompletedCount < 10) {
            Resource resource = null;
            try {
                resource = (Resource) unrequested.popHead();
                if (resource == null) {
                    break;
                }

                if (fileStatus[resource.dataType][resource.ID] != 0) {
                    filesLoaded++;
                }
                fileStatus[resource.dataType][resource.ID] = 0;
                requested.insertHead(resource);
                uncompletedCount++;
                request(resource);
                expectingData = true;
            } catch (Exception ex) {
                if (Constants.DEBUG_MODE) {
                    assert resource != null;
                    logger.log(Level.WARNING, "Expected data not found, type: {0}, id: {1}.", new Object[]{resource.dataType, resource.ID});
                }
                logger.log(Level.WARNING, "Expected data not found, type: {0}, id: {1}.", new Object[]{resource.dataType, resource.ID});
                ex.printStackTrace();
            }
        }
    }

    public void clearExtras() {
        synchronized (extras) {
            extras.clear();
        }
    }

    private void loadMandatory() {
        Resource resource;
        synchronized (mandatoryRequests) {
            resource = (Resource) mandatoryRequests.popHead();
        }
        while (resource != null) {
            expectingData = true;
            byte[] data = null;
            if (clientInstance.indices[0] != null) {
                int type = (resource.dataType + 1);
                data = clientInstance.indices[type].decompress(resource.ID);
            }
            if (Constants.JAGGRAB_ENABLED) {
                try {
                    if (!crcMatches(crcs[resource.dataType][resource.ID], data)) {
                        data = null;
                    }
                } catch (Exception e) {
                    if (Constants.DEBUG_MODE) { // TODO This is thrown even though we're downloading resources
                        logger.log(Level.WARNING, "Type: {0}, ID: {1}", new Object[]{resource.dataType, resource.ID});
                    }
                }
            }
            synchronized (mandatoryRequests) {
                if (data == null) {
                    unrequested.insertHead(resource);
                } else {
                    resource.buffer = data;
                    synchronized (complete) {
                        complete.insertHead(resource);
                    }
                }
                resource = (Resource) mandatoryRequests.popHead();
            }
        }
    }

    /**
     * Grabs the checksum of a file from the cache.
     *
     * @param expectedValue The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
     * @param crcData       The id of the file.
     * @return <code>True</code> if the CRC matches.
     */
    private boolean crcMatches(int expectedValue, byte[] crcData) {
        if (crcData == null || crcData.length < 2) {
            return false;
        }
        int length = crcData.length - 2;
        crc32.reset();
        crc32.update(crcData, 0, length);
        int crcValue = (int) crc32.getValue();
        return crcValue == expectedValue;
    }

    private void loadExtra() {
        while (uncompletedCount == 0 && completedCount < 10) {
            if (maximumPriority == 0) {
                break;
            }
            Resource resource;
            synchronized (extras) {
                resource = (Resource) extras.popHead();
            }
            while (resource != null) {
                if (fileStatus[resource.dataType][resource.ID] != 0) {
                    fileStatus[resource.dataType][resource.ID] = 0;
                    requested.insertHead(resource);
                    request(resource);
                    expectingData = true;
                    if (filesLoaded < totalFiles) {
                        filesLoaded++;
                    }
                    loadingMessage = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
                    completedCount++;
                    if (completedCount == 10) {
                        return;
                    }
                }
                synchronized (extras) {
                    resource = (Resource) extras.popHead();
                }
            }
            for (int type = 0; type < 4; type++) {
                byte data[] = fileStatus[type];
                int size = data.length;
                for (int file = 0; file < size; file++) {
                    if (data[file] == maximumPriority) {
                        data[file] = 0;
                        Resource newResource = new Resource();
                        newResource.dataType = type;
                        newResource.ID = file;
                        newResource.incomplete = false;
                        requested.insertHead(newResource);
                        request(newResource);
                        expectingData = true;
                        if (filesLoaded < totalFiles) {
                            filesLoaded++;
                        }
                        loadingMessage = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
                        completedCount++;
                        if (completedCount == 10) {
                            return;
                        }
                    }
                }
            }
            maximumPriority--;
        }
    }

    public boolean highPriorityMusic(int file) {
        return musicPriorities[file] == 1;
    }

    public void writeAll() {
        for (int i = 0; i < Constants.CACHE_INDICES; i++) {
            writeChecksumList(i);
            writeVersionList(i);
        }
    }

    public int getChecksum(int type, int id) {
        int crc = -1;
        byte[] data = clientInstance.indices[type + 1].decompress(id);
        if (data != null) {
            int length = data.length - 2;
            crc32.reset();
            crc32.update(data, 0, length);
            crc = (int) crc32.getValue();
        }
        return crc;
    }

    public int getVersion(int type, int id) {
        int version = -1;
        byte[] data = clientInstance.indices[type + 1].decompress(id);
        if (data != null) {
            int length = data.length - 2;
            version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
        }
        return version;
    }

    public void writeChecksumList(int type) {
        try {
            File folder = new File(Constants.getCachePath(true) + File.separator + "crcs");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(Constants.getCachePath(true) + File.separator + "crcs" + File.separator + type + "_crc.dat"));
            for (int index = 0; index < clientInstance.indices[type + 1].getFileCount(); index++) {
                dataOutputStream.writeInt(getChecksum(type, index));
            }
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeVersionList(int type) {
        try {
            File folder = new File(Constants.getCachePath(true) + File.separator + "versions");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(Constants.getCachePath(true) + File.separator + "versions" + File.separator + type + ".dat"));
            for (int index = 0; index < clientInstance.indices[type + 1].getFileCount(); index++) {
                dataOutputStream.writeShort(getVersion(type, index));
            }
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
