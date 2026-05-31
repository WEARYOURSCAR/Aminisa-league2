package com.example.data

data class StandingEntry(
    val rank: Int,
    val playerName: String,
    val played: Int,
    val won: Int,
    val lost: Int,
    val framesWon: Int,
    val framesLost: Int,
    val points: Int,
    val form: List<String> // list of "W", "L"
)

data class Fixture(
    val id: Int,
    val round: String,
    val playerA: String,
    val playerB: String,
    val scoreA: Int?,
    val scoreB: Int?,
    val date: String,
    val time: String,
    val venue: String,
    val isCompleted: Boolean
)

data class PrizeItem(
    val position: String,
    val prizeValue: String,
    val description: String,
    val icon: String
)

data class LeagueFeature(
    val title: String,
    val description: String,
    val iconName: String
)

object LeagueData {
    val prizes = listOf(
        PrizeItem("1st Place (Champion)", "₦1,500,000 + Gold Trophy", "Ultimate bragging rights, official tournament trophy, and elite club membership.", "🏆"),
        PrizeItem("2nd Place (Runner-Up)", "₦750,000 + Silver Medal", "Prestigious second place medal and direct seed into next tournament.", "🥈"),
        PrizeItem("3rd Place (Third-Runner)", "₦350,000 + Bronze Medal", "Bronze championship medal and tournament official cue asset accessory.", "🥉"),
        PrizeItem("Highest Tournament Break", "₦150,000 Award Bonus", "Awarded to the player with the single highest break sequence above 50.", "🎯")
    )

    val features = listOf(
        LeagueFeature("Professional Officiating", "EPBSA & local master certified referees officiate all tournament bracket and group stage fixtures.", "Gavel"),
        LeagueFeature("HD Stream & Live Scores", "Matches are streamed live on official ASCL platforms with professional overlay scoreboards.", "Tv"),
        LeagueFeature("Premium Venue Playing Arena", "Games played on elite standard steel-block cushions with high-speed tournament green felt.", "LocationOn"),
        LeagueFeature("State Dashboard Records", "Dynamic tracking of cueing stats, average frame durations, pot success, and historical break points.", "Star")
    )

    val standings = listOf(
        StandingEntry(1, "Wale 'The Sniper' Adeleke", 12, 10, 2, 42, 21, 30, listOf("W", "W", "W", "L", "W")),
        StandingEntry(2, "Chukwuma Okafor", 12, 9, 3, 39, 24, 27, listOf("W", "W", "L", "W", "W")),
        StandingEntry(3, "Sani Bello Usman", 12, 8, 4, 35, 27, 24, listOf("L", "W", "W", "W", "L")),
        StandingEntry(4, "Tunde 'Cushion' Williams", 12, 7, 5, 33, 29, 21, listOf("W", "L", "W", "L", "W")),
        StandingEntry(5, "Ibrahim 'The Master' Haruna", 12, 7, 5, 31, 30, 21, listOf("L", "W", "L", "W", "W")),
        StandingEntry(6, "Daniel 'Double-Kiss' Effiong", 12, 6, 6, 28, 28, 18, listOf("W", "L", "W", "W", "L")),
        StandingEntry(7, "Nze Kevin Kalu", 12, 5, 7, 25, 32, 15, listOf("L", "W", "L", "L", "W")),
        StandingEntry(8, "Mustapha Gombe", 12, 4, 8, 22, 35, 12, listOf("W", "L", "L", "L", "L")),
        StandingEntry(9, "Victor 'Side-Spin' George", 12, 3, 9, 18, 38, 9, listOf("L", "L", "W", "L", "L")),
        StandingEntry(10, "Kunle Jinadu", 12, 1, 11, 12, 44, 3, listOf("L", "L", "L", "L", "L"))
    )

    val fixtures = listOf(
        Fixture(1, "Upcoming - Round 13", "Wale 'The Sniper' Adeleke", "Sani Bello Usman", null, null, "June 12, 2026", "17:00", "Aminisa Club House, Abuja (Table 1)", false),
        Fixture(2, "Upcoming - Round 13", "Chukwuma Okafor", "Tunde 'Cushion' Williams", null, null, "June 12, 2026", "19:00", "Aminisa Club House, Abuja (Table 2)", false),
        Fixture(3, "Upcoming - Round 13", "Ibrahim 'The Master' Haruna", "Daniel 'Double-Kiss' Effiong", null, null, "June 13, 2026", "16:00", "Aminisa Club House, Abuja (Table 1)", false),
        Fixture(4, "Upcoming - Round 13", "Nze Kevin Kalu", "Mustapha Gombe", null, null, "June 13, 2026", "18:20", "Aminisa Club House, Abuja (Table 2)", false),
        
        Fixture(5, "Completed - Round 12", "Wale 'The Sniper' Adeleke", "Kunle Jinadu", 4, 1, "May 28, 2026", "17:00", "Aminisa Club House, Abuja (Table 1)", true),
        Fixture(6, "Completed - Round 12", "Chukwuma Okafor", "Victor 'Side-Spin' George", 4, 0, "May 28, 2026", "19:30", "Aminisa Club House, Abuja (Table 2)", true),
        Fixture(7, "Completed - Round 12", "Sani Bello Usman", "Mustapha Gombe", 4, 2, "May 29, 2026", "16:00", "Aminisa Club House, Abuja (Table 1)", true),
        Fixture(8, "Completed - Round 12", "Tunde 'Cushion' Williams", "Daniel 'Double-Kiss' Effiong", 3, 4, "May 29, 2026", "18:45", "Aminisa Club House, Abuja (Table 2)", true)
    )
    
    val testimonials = listOf(
        Pair("Aminu Aminu", "ASCL Founder & Snooker Enthusiast:\n'Our mission with ASCL is simple: raise snooker playing standards, and establish Abuja as a premium snooker sporting cluster in West Africa. This registration module elevates the professionalism of our league.'"),
        Pair("Captain Wale", "Reigning Season 2 Bronze Champion:\n'The structural competitive intensity of ASCL is unmatched. Highly organized, elite refereeing, and premium table fast conditions. If you cue snooker in Abuja, ASC is the place to be.'")
    )
}
