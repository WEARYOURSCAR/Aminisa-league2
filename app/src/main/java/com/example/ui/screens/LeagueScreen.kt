package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeagueData
import com.example.ui.components.WebFooter

@Composable
fun LeagueScreen(
    modifier: Modifier = Modifier,
    isWebsiteMode: Boolean = false
) {
    var selectedSubTab by remember { mutableStateOf("Standings") }
    val tabOptions = listOf("Standings", "Fixtures", "Leaderboard")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp)
    ) {
        Text(
            text = "LEAGUE CENTRAL",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Official results, tables, and fixtures for Abuja's premier tournament.",
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Sub tab option rows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141414), RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabOptions.forEach { tab ->
                val isSelected = selectedSubTab == tab
                Button(
                    onClick = { selectedSubTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF00A651) else Color.Transparent,
                        contentColor = if (isSelected) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = tab.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedSubTab) {
            "Standings" -> StandingsTab(isWebsiteMode = isWebsiteMode)
            "Fixtures" -> FixturesTab(isWebsiteMode = isWebsiteMode)
            "Leaderboard" -> LeaderboardTab(isWebsiteMode = isWebsiteMode)
        }
    }
}

@Composable
fun StandingsTab(isWebsiteMode: Boolean = false) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            // Table Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E), RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "R", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(28.dp))
                Text(text = "PLAYER NAME", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(text = "P", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text(text = "W", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text(text = "F-DIFF", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(44.dp), textAlign = TextAlign.Center)
                Text(text = "PTS", color = Color(0xFFD4AF37), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp), textAlign = TextAlign.End)
            }
        }

        items(LeagueData.standings) { entry ->
            val isTopThree = entry.rank <= 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF141414), RoundedCornerShape(6.dp))
                    .border(
                        0.5.dp, 
                        if (isTopThree) Color(0xFFD4AF37).copy(alpha = 0.2f) else Color.Transparent, 
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rank number
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = entry.rank.toString(),
                        color = when (entry.rank) {
                            1 -> Color(0xFFD4AF37)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color.White
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Player info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.playerName,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    // Form indicator circles
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        entry.form.forEach { outcome ->
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        if (outcome == "W") Color(0xFF00A651) else Color(0xFFE53935), 
                                        CircleShape
                                    )
                            )
                        }
                    }
                }

                // Played
                Text(
                    text = entry.played.toString(),
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.Center
                )

                // Won
                Text(
                    text = entry.won.toString(),
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.Center
                )

                // Frames Difference
                val diff = entry.framesWon - entry.framesLost
                val diffStr = if (diff > 0) "+$diff" else "$diff"
                Text(
                    text = diffStr,
                    color = if (diff > 0) Color(0xFF00A651) else Color.LightGray,
                    fontSize = 11.sp,
                    modifier = Modifier.width(44.dp),
                    textAlign = TextAlign.Center
                )

                // Points accumulated
                Text(
                    text = entry.points.toString(),
                    color = Color(0xFFD4AF37),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.End
                )
            }
        }

        item {
            if (isWebsiteMode) {
                Spacer(modifier = Modifier.height(32.dp))
                WebFooter()
            }
        }
    }
}

@Composable
fun FixturesTab(isWebsiteMode: Boolean = false) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(LeagueData.fixtures) { fixture ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        0.5.dp, 
                        if (fixture.isCompleted) Color.White.copy(0.04f) else Color(0xFF00A651).copy(0.2f), 
                        RoundedCornerShape(8.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Match round label & Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fixture.round.uppercase(),
                            color = if (fixture.isCompleted) Color.Gray else Color(0xFF00A651),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    if (fixture.isCompleted) Color.DarkGray.copy(0.4f) else Color(0xFFD4AF37).copy(0.12f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (fixture.isCompleted) "COMPLETED" else "LIVE SCHEDULE",
                                color = if (fixture.isCompleted) Color.LightGray else Color(0xFFD4AF37),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Player Duel & Score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Player A
                        Text(
                            text = fixture.playerA,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )

                        // Score Block
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            if (fixture.isCompleted) {
                                Text(
                                    text = fixture.scoreA.toString(),
                                    color = Color(0xFFD4AF37),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.width(22.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = ":",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.width(10.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = fixture.scoreB.toString(),
                                    color = Color(0xFFD4AF37),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.width(22.dp),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    text = "VS",
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(44.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Player B
                        Text(
                            text = fixture.playerB,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Divider(color = Color.White.copy(0.04f), thickness = 0.5.dp)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date and Location footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${fixture.date} @ ${fixture.time}", color = Color.Gray, fontSize = 10.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Aminisa Arena", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        item {
            if (isWebsiteMode) {
                Spacer(modifier = Modifier.height(32.dp))
                WebFooter()
            }
        }
    }
}

@Composable
fun LeaderboardTab(isWebsiteMode: Boolean = false) {
    val rankingData = listOf(
        RatingEntry(1, "Wale 'The Sniper' Adeleke", 2390, 102, "Maitama"),
        RatingEntry(2, "Chukwuma Okafor", 2310, 84, "Wuse 2"),
        RatingEntry(3, "Sani Bello Usman", 2250, 78, "Gwarinpa"),
        RatingEntry(4, "Tunde 'Cushion' Williams", 2180, 92, "Asokoro"),
        RatingEntry(5, "Ibrahim 'The Master' Haruna", 2140, 71, "Garki"),
        RatingEntry(6, "Daniel 'Double-Kiss' Effiong", 2090, 68, "Wuse 1"),
        RatingEntry(7, "Nze Kevin Kalu", 1980, 62, "Maitama"),
        RatingEntry(8, "Mustapha Gombe", 1910, 54, "Asokoro")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(rankingData) { rating ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        0.5.dp, 
                        if (rating.rank == 1) Color(0xFFD4AF37).copy(0.3f) else Color.Transparent, 
                        RoundedCornerShape(8.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position Badge circle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                when (rating.rank) {
                                    1 -> Color(0xFFD4AF37)
                                    2 -> Color(0xFFC0C0C0)
                                    3 -> Color(0xFFCD7F32)
                                    else -> Color.DarkGray
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = rating.rank.toString(),
                            color = if (rating.rank <= 3) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = rating.name,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                            Icon(imageVector = Icons.Default.Room, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "Abuja Arena • Resident: ${rating.residence}", color = Color.Gray, fontSize = 9.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        // Cue Rank Rating
                        Text(
                            text = "${rating.rating} QP",
                            color = Color(0xFF00A651),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // Peak Break Point
                        Text(
                            text = "Peak Break: ${rating.highBreak}",
                            color = Color(0xFFD4AF37),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            if (isWebsiteMode) {
                Spacer(modifier = Modifier.height(32.dp))
                WebFooter()
            }
        }
    }
}

data class RatingEntry(
    val rank: Int,
    val name: String,
    val rating: Int,
    val highBreak: Int,
    val residence: String
)
