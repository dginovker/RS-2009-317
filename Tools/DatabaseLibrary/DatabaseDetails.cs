using System.IO;
using YamlDotNet.Serialization;
using YamlDotNet.Serialization.NamingConventions;

namespace DatabaseLibrary
{
    public class DatabaseDetails
    {

        // The instance we access using getSession.
        private static DatabaseDetails Instance;

        // Singleton pattern.
        public static DatabaseDetails GetDetails
        {
            get
            {
                if (Instance == null)
                {
                    Instance = ParseDetails();
                }
                return Instance;
            }
        }

        // Constructor used for the Singleton
        public DatabaseDetails() { }

        const string FILE_PATH = "../../../../Server/database.yml";

        public override string ToString()
        {
            return $"[host:{host} port:{port} dname:{dbname} username:{username} password:{password}]";
        }

        public string host { get; set; }

        public int port { get; set; }

        public string dbname { get; set; }

        public string username { get; set; }

        public string password { get; set; }

        private static DatabaseDetails ParseDetails()
        {
            using (var input = File.OpenText(FILE_PATH))
            {
                var deserializerBuilder = new DeserializerBuilder().WithNamingConvention(new CamelCaseNamingConvention());
                var deserializer = deserializerBuilder.Build();
                var result = deserializer.Deserialize<DatabaseDetails>(input);

                return result;
            }
        }
    }
}
