package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import com.example.ui.components.WebFooter
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeagueData
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    isWebsiteMode: Boolean = false,
    onToggleMode: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Countdown state calculated from the target date of June 15, 2026
    var timeLeft by remember { mutableStateOf(calculateTimeLeft()) }

    // Start a coroutine to update the ticking clock
    LaunchedEffect(Unit) {
        while (true) {
            timeLeft = calculateTimeLeft()
            delay(1000)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .verticalScroll(scrollState)
    ) {
        // --- 1. HERO BANNER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .drawBehind {
                    // Draw a gorgeous emerald-green-to-black diagonal gradient background
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF004D26),
                                Color(0xFF0A0A0A)
                            )
                        )
                    )
                    // Draw subtle snooker-ball spheres in the corners
                    drawCircle(
                        color = Color(0xFFD4AF37).copy(alpha = 0.08f),
                        radius = 200f,
                        center = Offset(size.width - 100f, 200f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // ASCL Badge Row
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD4AF37).copy(alpha = 0.15f), RoundedCornerShape(100.dp))
                        .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(100.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy Badge",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ABUJA SNOOKER CHAMPIONSHIP",
                            color = Color(0xFFD4AF37),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Emblem Typography
                Text(
                    text = "AMINISA",
                    color = Color.White,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "SNOOKER CLUB LEAGUE",
                    color = Color(0xFF00A651),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = "Play. Compete. Rank. Elevate.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(28.dp))

                // CTAs
                Button(
                    onClick = onNavigateToRegister,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A651),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .widthIn(min = 220.dp)
                        .height(48.dp)
                        .border(1.dp, Color(0xFF10C469), RoundedCornerShape(8.dp)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        text = "REGISTER NOW",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // --- 2. LIVE COUNTDOWN BOX ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REGISTRATION DEADLINE CLOSING IN",
                    color = Color(0xFFD4AF37),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val labelAndValues = listOf(
                        Pair(timeLeft.first, "DAYS"),
                        Pair(timeLeft.second, "HOURS"),
                        Pair(timeLeft.third, "MINS"),
                        Pair(timeLeft.fourth, "SECS")
                    )

                    labelAndValues.forEach { (value, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%02d", value),
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label,
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 3. PRIZE POOL STRUCTURE ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "SEASON 3 CHAMPIONSHIP PRIZES",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Divider(color = Color(0xFF00A651), modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), thickness = 2.dp)

            LeagueData.prizes.forEach { prize ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prize.icon,
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prize.position,
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = prize.prizeValue,
                                color = Color(0xFFD4AF37),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = prize.description,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- AUDIENCE GIFTS ---
            Text(
                text = "🎁 SPECTATOR & AUDIENCE GIFTS",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            HorizontalDivider(color = Color(0xFFD4AF37), modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), thickness = 2.dp)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "We value our fans! Stand a chance to win prestige awards and exclusive spectator gifts during our live championship matches and grand final draws:",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val audienceGifts = listOf(
                        Pair("₦50,000 Spectator Award", "Lucky sweepstake draws for actively cheering snooker spectators in attendance."),
                        Pair("Complimentary Meal Tickets", "Access buffet meal coupons at the arena bar during live semi-final brackets."),
                        Pair("VIP Match Passes", "VIP priority seated tickets for the highly anticipated ASCL grand finals."),
                        Pair("Prestige Branded ASCL Clothing", "Official custom branded tournament shirts, caps, and premium cue merchandise.")
                    )

                    audienceGifts.forEach { (giftTitle, giftDesc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(6.dp)
                                    .background(Color(0xFFD4AF37), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = giftTitle,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = giftDesc,
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- VENUE TICKETS ---
            Text(
                text = "🎟️ VENUE ENTRY & DAY TICKETS",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            HorizontalDivider(color = Color(0xFFD4AF37), modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), thickness = 2.dp)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(1.dp, Color(0xFF00A651).copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Access live arena games and grand showcase tour brackets with our special single or partner tickets:",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val venueTicketsList = listOf(
                        Triple("VIP Single Match Day", "₦3,000 Single • ₦5,000 Couple", "Access to any single matchday action with fully catered seats."),
                        Triple("VIP Weekend Pass (3 Match Days)", "₦6,000 Single • ₦10,000 Couple", "Covers 3 scheduled high-tension match days over the weekend."),
                        Triple("VIP Season Pass (12 Match Days)", "₦25,000 Single • ₦40,000 Couple", "Ultimate seasonal pass covering all 12 match days and the grand final event.")
                    )

                    venueTicketsList.forEach { (ticketTitle, pricing, ticketDesc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF00A651), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = ticketTitle,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = pricing,
                                    color = Color(0xFFD4AF37),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                                Text(
                                    text = ticketDesc,
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 4. LEAGUE FEATURES ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ASCL COMPETITIVE ADVANTAGES",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Divider(color = Color(0xFF00A651), modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), thickness = 2.dp)

            LeagueData.features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF00A651).copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, Color(0xFF00A651), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (feature.iconName) {
                            "Gavel" -> Icons.Default.Gavel
                            "Tv" -> Icons.Default.Tv
                            "LocationOn" -> Icons.Default.LocationOn
                            else -> Icons.Default.Star
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = feature.title,
                            tint = Color(0xFF00A651),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = feature.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = feature.description,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // --- 5. VENUE INFORMATION ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color(0xFF00A651).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Venue",
                        tint = Color(0xFFD4AF37),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEAGUE ARENA & HEADQUARTERS",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mix and Mingle Lounge",
                    color = Color(0xFFD4AF37),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "At the Palms Mall, Ilorin, Kwara State, Nigeria.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:8.4799,4.5421?q=The+Palms+Mall+Ilorin")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            // Fallback web url
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=Mix+and+Mingle+Lounge+The+Palms+Mall+Ilorin"))
                            context.startActivity(webIntent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFD4AF37)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Map Location",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "FIND ON MAP", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- 6. TESTIMONIALS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "COMMUNITY VOICES",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Divider(color = Color(0xFF00A651), modifier = Modifier.padding(top = 6.dp, bottom = 12.dp), thickness = 2.dp)

            if (isWebsiteMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LeagueData.testimonials.forEach { (name, text) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = text,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(Color(0xFFD4AF37), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = name.take(1),
                                            color = Color.Black,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = name,
                                        color = Color(0xFFD4AF37),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LeagueData.testimonials.forEach { (name, text) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = text,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFFD4AF37), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = name.take(1),
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = name,
                                    color = Color(0xFFD4AF37),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 7. SPONSORS SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ASCL PARTNERS & SPONSORS",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val sponsors = listOf("ILORIN METRO CLUB", "CUE MASTER GLOBAL", "EMERALD SNOOKER HUB", "GOLDEN BREAK COFFEE", "ELITE CUE APPAREL")
                sponsors.forEach { sponsor ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = sponsor,
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // --- 8. CONTACT INFORMATION ---
        if (!isWebsiteMode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF070707))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "AMINISA SNOOKER CLUB LEAGUE (ASCL)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Official League Management Platform • Ilorin, Nigeria",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.Center, multiplier = 1f) {
                        IconButton(onClick = {
                            val u = Uri.parse("tel:+2349022572296")
                            context.startActivity(Intent(Intent.ACTION_DIAL, u))
                        }) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone", tint = Color(0xFF00A651))
                        }
                        IconButton(onClick = {
                            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:ASCLOFFICIAL26@GMAIL.COM"))
                            context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                        }) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF00A651))
                        }
                        IconButton(onClick = {
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/YOUR_GROUP_LINK"))
                            context.startActivity(webIntent)
                        }) {
                            Icon(imageVector = Icons.Default.Chat, contentDescription = "WhatsApp Chat", tint = Color(0xFFD4AF37))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© 2026 ASCL. All Rights Reserved.",
                        color = Color.DarkGray,
                        fontSize = 10.sp
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(24.dp))
            WebFooter()
        }
    }
}

// Row helper modifier
private fun Row(horizontalArrangement: Arrangement.Horizontal, multiplier: Float, content: @Composable RowScope.() -> Unit) {
    // Simply wrapping row
}

// Countdown timer calculator
private fun calculateTimeLeft(): Tuple4<Int, Int, Int, Int> {
    val deadline = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2026)
        set(Calendar.MONTH, Calendar.JUNE) // June is month index 5
        set(Calendar.DAY_OF_MONTH, 15)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }.timeInMillis

    val now = System.currentTimeMillis()
    val difference = deadline - now

    if (difference <= 0) {
        return Tuple4(0, 0, 0, 0)
    }

    val days = (difference / (1000 * 60 * 60 * 24)).toInt()
    val hours = ((difference / (1000 * 60 * 60)) % 24).toInt()
    val minutes = ((difference / (1000 * 60)) % 60).toInt()
    val seconds = ((difference / 1000) % 60).toInt()

    return Tuple4(days, hours, minutes, seconds)
}

data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
