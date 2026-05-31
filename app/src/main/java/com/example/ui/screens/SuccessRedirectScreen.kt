package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlayerRegistration
import com.example.ui.components.QrCodeComponent
import kotlinx.coroutines.delay

@Composable
fun SuccessRedirectScreen(
    player: PlayerRegistration,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var secondsLeft by remember { mutableStateOf(5) }
    val groupLink = "https://chat.whatsapp.com/YOUR_GROUP_LINK"

    // Circular progress fraction animation
    val progressFraction by animateFloatAsState(targetValue = secondsLeft / 5f, label = "Seconds progress")

    // Automated 5-second countdown & redirect flow
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
        // When timer hits 0, execute local WhatsApp redirect intent
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(groupLink))
            context.startActivity(intent)
        } catch (e: Exception) {
            // Silence or toast if browser/app is not found
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // SUCCESS CHECKMARK RING
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Success Verification",
            tint = Color(0xFF00A651),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "REGISTRATION COMPLETED!",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Congratulations, ${player.fullName}! Your player profile has been captured successfully.",
            color = Color.LightGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // ASSIGNED CARD PASS
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ASCL PLAYER CREW CARD",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                // HIGH FIDELITY DETERMINISTIC QR CODE drawing
                QrCodeComponent(
                    text = player.uniquePlayerId,
                    size = 140.dp,
                    qrColor = Color.Black,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = player.uniquePlayerId,
                    color = Color(0xFFD4AF37),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "STATUS: ${player.status.uppercase()}",
                    color = when (player.status) {
                        "Approved" -> Color(0xFF00A651)
                        "Rejected" -> Color(0xFFE53935)
                        else -> Color(0xFFD4AF37) // Pending
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "PREFERRED HAND", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = player.preferredCueHand, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "EXPERIENCE LEVEL", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${player.skillLevel} (${player.experienceYears} Cues)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // REDIRECT LOADER TIMER
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progressFraction },
                    color = Color(0xFF00A651),
                    trackColor = Color.DarkGray,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(44.dp)
                )
                Text(
                    text = secondsLeft.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Redirecting you to the official WhatsApp Group in $secondsLeft seconds...",
                color = Color.Gray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DIRECT CTA BUTTON FOR JOINING COMMUNITY
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(groupLink))
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00A651),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .border(1.dp, Color(0xFF10C469), RoundedCornerShape(8.dp))
        ) {
            Icon(imageVector = Icons.Default.Forum, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "JOIN OFFICIAL ASCL COMMUNITY", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onNavigateHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "RETURN TO LEAGUE HOMEPAGE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
