package bot

/**
 * Keep in order of greatest -> least!
 */
enum class GielinorDiscordRoles(val discordId: String?, val forumsId: Int?) {
    //    ADMINISTRATOR("232500822917906433", 4),
//    DEVELOPER("232788167093256192", 12),
//    GAME_MODERATOR(null, 7),
//    FORUMS_MODERATOR("232506580694728704", 6),
    ZENYTE_MEMBER("356104546436644865", 19),
    DIAMOND_MEMBER("356103785073868810", 16),
    ONYX_MEMBER("356104382422450176", 18),
    DRAGONSTONE_MEMBER("232789334380511233", 17),
    RUBY_MEMBER("232788468541947905", 10),
    EMERALD_MEMBER("356103801947815939", 9),
    SAPPHIRE_MEMBER("232788456445575168", 8),
    //    DISCORD_MAINTENANCE("232784752036413441", null),
    MEMBER_OF_THE_MONTH("234611080146452480", 26),
    //    RESPECTED(null, 24),
    STREAMER("234303853963640842", 21),
    YOUTUBE("234304509508059147", 20),
    //    EX_STAFF(null, 28),
    VETERAN("234303820023201793", 23),
    GUIDE_MAKER("234303612191375361", 25),
    /*GFX_ARTIST(null, 22),
    ULTIMATE_IRONMAN(null, 15),
    HARDCORE_IRONMAN(null, 14),
    IRONMAN(null, 13),
    MEMBERS(null, 3),*/
    ;

    companion object {
        fun forForumsId(forumsId: Int): GielinorDiscordRoles? {
            values().forEach { if (forumsId == it.forumsId) return it }
            return null
        }

        fun forDiscordId(discordId: String?): GielinorDiscordRoles? {
            values().forEach { if (discordId == it.discordId) return it }
            return null
        }
    }
}
