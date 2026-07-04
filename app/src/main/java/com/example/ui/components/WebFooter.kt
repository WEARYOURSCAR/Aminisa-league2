package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WebFooter(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F0F))
            .border(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.15f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(
                    text = "AMINISA SPORT CLUB LEAGUE (ASCL)",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ilorin's ultimate championship cueists tournament. Real-time standings tracking, verified profiles, automated registration rosters, and premium physical trophies.",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ASCL INFORMATION",
                    color = Color(0xFFD4AF37),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Entry Slot Fee: ₦1,000\n• Partner: SquadCo\n• Launch Date: June 15, 2026\n• Location: Palms Mall, Ilorin",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PORTAL ENQUIRIES",
                    color = Color(0xFFD4AF37),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:aminisasport@gmail.com"))
                        try { context.startActivity(Intent.createChooser(emailIntent, "Send Email")) } catch (e: Exception) {}
                    }
                ) {
                    Text(
                        text = "✉️ ",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "aminisasport@gmail.com",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+2349022572296"))
                        try { context.startActivity(dialIntent) } catch (e: Exception) {}
                    }
                ) {
                    Text(
                        text = "📞 ",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "+2349022572296",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💬 Elite Cueists Team • Ilorin",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@aminisa.sport?_r=1&_t=ZS-97klCe15xa8"))
                        try { context.startActivity(browserIntent) } catch (e: Exception) {}
                    }
                ) {
                    Text(
                        text = "📱 TikTok: ",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "@aminisa.sport",
                        color = Color(0xFFD4AF37),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61591824231534"))
                        try { context.startActivity(browserIntent) } catch (e: Exception) {}
                    }
                ) {
                    Text(
                        text = "📘 Facebook: ",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Aminisa Sport",
                        color = Color(0xFFD4AF37),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "© 2026 ASCL Ilorin. Standard Web-Portal Layout Interface. All Rights Reserved.",
                color = Color.DarkGray,
                fontSize = 10.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Registration Terms",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.clickable {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aminisasnooker.com/terms"))
                        try { context.startActivity(browserIntent) } catch (e: Exception) {}
                    }
                )
                Text(
                    text = "Code of Conduct",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.clickable {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aminisasnooker.com/rules"))
                        try { context.startActivity(browserIntent) } catch (e: Exception) {}
                    }
                )
            }
        }
    }
}
