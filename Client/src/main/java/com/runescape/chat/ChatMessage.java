package com.runescape.chat;

import java.util.Objects;

/**
 * Represents a sent chat message.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ChatMessage {

    /**
     * The type.
     */
    private final int type;
    /**
     * The name.
     */
    private final String name;
    /**
     * The text.
     */
    private final String text;
    /**
     * The image id.
     */
    private final int[] imageIds;
    /**
     * The title.
     */
    private String title = "";
    /**
     * The title suffix status.
     */
    private boolean titleSuffix;
    /**
     * The URL text (if any).
     */
    private String urlText;
    /**
     * The URL (if any).
     */
    private String url;
    /**
     * The clan name.
     */
    private String clanName;
    /**
     * The delay before clearing.
     */
    private long clearDelay;
    /**
     * The filter type, 1 if it can be filtered.
     */
    private int filterType = 0;

    /**
     * Creates a new {@code ChatMessage}.
     *
     * @param type        The type.
     * @param name        The name.
     * @param text        The text.
     * @param imageIds     The image ids
     * @param title       The title.
     * @param titleSuffix The title suffix status.
     * @param clearDelay  The delay before clearing.
     */
    public ChatMessage(int type, String name, String text, String title, boolean titleSuffix, long clearDelay, int filterType, int...imageIds) {
        this.type = type;
        this.name = name;
        this.text = text;
        this.imageIds = imageIds;
        this.title = title == null ? "" : title;
        this.titleSuffix = titleSuffix;
        this.clearDelay = clearDelay;
        this.filterType = filterType;
    }

    /**
     * Creates a new {@code ChatMessage}.
     *
     * @param type        The type.
     * @param name        The name.
     * @param text        The text.
     * @param imageIds     The image ids
     * @param title       The title.
     * @param titleSuffix The title suffix status.
     */
    public ChatMessage(int type, String name, String text, String title, boolean titleSuffix, int...imageIds) {
        this(type, name, text, title, titleSuffix, -1, 0, imageIds);
    }

    /**
     * Creates a new {@code ChatMessage}.
     *
     * @param type     The type.
     * @param name     The name.
     * @param text     The text.
     * @param imageIds  The image ids
     * @param clanName The clan name.
     */
    public ChatMessage(int type, String name, String text, String clanName, int...imageIds) {
        this.type = type;
        this.name = name;
        this.text = text;
        this.imageIds = imageIds;
        this.clanName = clanName;
    }

    /**
     * Gets the {@link com.runescape.chat.ChatMessageType} for this <code>ChatMessage</code>.
     *
     * @return The {@link com.runescape.chat.ChatMessageType}.
     */
    public ChatMessageType getChatMessageType() {
        return ChatMessageType.forId(type);
    }

    /**
     * Gets the type.
     *
     * @return The type.
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the name with the title.
     *
     * @return The name.
     */
    public String getName() {
        if (hasTitle()) {
            return getChatIcons() + (!titleSuffix ? (title + name) : (name + title));
        }
        return getChatIcons() + name;
    }

    /**
     * Gets the name.
     *
     * @param useTitle Whether or not to use the title.
     * @return The name.
     */
    public String getName(boolean useTitle) {
        return useTitle ? getName() : (getChatIcons() + name);
    }

    public String getChatIcons() {
        StringBuilder sb = new StringBuilder();
        for (int imageId : getImageIds()) {
            if (imageId > -1) {
                sb.append("<img=" + imageId + ">");
            }
        }
        return sb.toString();
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getNameClean() {
        return name;
    }

    /**
     * Gets the name cleaned with a title.
     *
     * @return The name with the title.
     */
    public String getNameCleanTitle() {
        return (!titleSuffix ? (title + name) : (name + title));
    }

    /**
     * Gets the text.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the image id.
     *
     * @return The image id.
     */
    public int[] getImageIds() {
        for (int i = 0; i < imageIds.length; i++) {
            if (imageIds[i] >= 255) {
                imageIds[i] = -1;
            }
        }
        return imageIds;
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the title suffix status.
     *
     * @return {@code True} if so.
     */
    public boolean isTitleSuffix() {
        return titleSuffix;
    }

    /**
     * Gets whether or not this chat message has a title.
     *
     * @return {@code True} if so.
     */
    public boolean hasTitle() {
        return (title != null && !Objects.equals(title, ""));
    }

    /**
     * Gets the URL text (if any).
     *
     * @return The URL text.
     */
    public String getUrlText() {
        return urlText;
    }

    /**
     * Sets the URL text.
     *
     * @param urlText The URL text.
     */
    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    /**
     * Gets the URL.
     *
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url The URL text.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the clan name.
     *
     * @return The clan name.
     */
    public String getClanName() {
        return clanName;
    }

    /**
     * Gets the delay before clearing.
     *
     * @return The clear delay.
     */
    public long getClearDelay() {
        return clearDelay;
    }

    /**
     * Sets the delay before clearing.
     *
     * @param clearDelay The clear delay.
     */
    public void setClearDelay(long clearDelay) {
        this.clearDelay = clearDelay;
    }

    /**
     * Decreases the delay before clearing.
     */
    public void decreaseClearDelay() {
        this.clearDelay--;
    }

    /**
     * Gets the filter type.
     *
     * @return The filter type.
     */
    public int getFilterType() {
        return filterType;
    }
}
