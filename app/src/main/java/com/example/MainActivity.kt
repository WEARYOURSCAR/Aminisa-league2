package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlayerRegistration
import com.example.ui.LeagueViewModel
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeagueScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SuccessRedirectScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: LeagueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentTab by remember { mutableStateOf("Home") }
                var activeSuccessPlayer by remember { mutableStateOf<PlayerRegistration?>(null) }
                // Default to true as the user specifically requested "make it a website mode for now"
                var isWebsiteMode by remember { mutableStateOf(true) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0A0A0A))
                ) {
                    // 1. Desktop Web Header Bar (sticky at the top in website mode)
                    if (isWebsiteMode && activeSuccessPlayer == null) {
                        WebHeaderBar(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it },
                            onToggleMode = { isWebsiteMode = it }
                        )
                    } else if (!isWebsiteMode && activeSuccessPlayer == null) {
                        // Mobile App Top Banner with a quick switch to Website Mode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F0F0F))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .windowInsetsPadding(WindowInsets.statusBars),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ASCL MOBILE RIDER", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Button(
                                onClick = { isWebsiteMode = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                                border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(0.4f)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("TRY WEBSITE MODE", color = Color(0xFFD4AF37), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // 2. Main content block inside an adaptive responsive frame
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Scaffold(
                            modifier = if (isWebsiteMode && activeSuccessPlayer == null) {
                                Modifier
                                    .fillMaxHeight()
                                    .widthIn(max = 1200.dp)
                                    .border(0.5.dp, Color(0xFFD4AF37).copy(0.12f))
                            } else {
                                Modifier.fillMaxSize()
                            },
                            bottomBar = {
                                // Bottom Navigation (Only visible in Mobile App Mode and not on success redirect screen)
                                if (!isWebsiteMode && activeSuccessPlayer == null) {
                                    NavigationBar(
                                        containerColor = Color(0xFF0F0F0F),
                                        tonalElevation = 8.dp,
                                        modifier = Modifier
                                            .border(0.5.dp, Color(0xFFD4AF37).copy(0.12f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                            .windowInsetsPadding(WindowInsets.navigationBars)
                                    ) {
                                        NavigationBarItem(
                                            selected = currentTab == "Home",
                                            onClick = { currentTab = "Home" },
                                            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                                            label = { Text("Home", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = Color(0xFF00A651),
                                                indicatorColor = Color(0xFF00A651),
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == "Register",
                                            onClick = { currentTab = "Register" },
                                            icon = { Icon(imageVector = Icons.Default.AppRegistration, contentDescription = "Register") },
                                            label = { Text("Register", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = Color(0xFF00A651),
                                                indicatorColor = Color(0xFF00A651),
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == "League",
                                            onClick = { currentTab = "League" },
                                            icon = { Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "League Central") },
                                            label = { Text("League", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = Color(0xFF00A651),
                                                indicatorColor = Color(0xFF00A651),
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )

                                        NavigationBarItem(
                                            selected = currentTab == "Admin",
                                            onClick = { currentTab = "Admin" },
                                            icon = { Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                                            label = { Text("Admin", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = Color(0xFF00A651),
                                                indicatorColor = Color(0xFF00A651),
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF0A0A0A))
                                    .padding(innerPadding)
                            ) {
                                val player = activeSuccessPlayer
                                if (player != null) {
                                    SuccessRedirectScreen(
                                        player = player,
                                        onNavigateHome = {
                                            activeSuccessPlayer = null
                                            currentTab = "Home"
                                        }
                                    )
                                } else {
                                    when (currentTab) {
                                        "Home" -> HomeScreen(
                                            onNavigateToRegister = { currentTab = "Register" },
                                            isWebsiteMode = isWebsiteMode,
                                            onToggleMode = { isWebsiteMode = it }
                                        )
                                        "Register" -> RegisterScreen(
                                            viewModel = viewModel,
                                            onRegistrationSuccess = { registeredPlayer ->
                                                activeSuccessPlayer = registeredPlayer
                                            },
                                            isWebsiteMode = isWebsiteMode
                                        )
                                        "League" -> LeagueScreen(
                                            isWebsiteMode = isWebsiteMode
                                        )
                                        "Admin" -> AdminScreen(
                                            viewModel = viewModel,
                                            isWebsiteMode = isWebsiteMode
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WebHeaderBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    onToggleMode: (Boolean) -> Unit
) {
    Surface(
        color = Color(0xFF0F0F0F),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.15f)),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onTabSelected("Home") }
                    .padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color(0xFF00A651), CircleShape)
                        .border(1.5.dp, Color(0xFFD4AF37), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("8", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AMINISA SNOOKER LEAGUE",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(Color(0xFF10C469), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "OFFICIAL WEB PORTAL • ABUJA CP", color = Color(0xFFD4AF37), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Desktop Horizontal Navigation Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val navigationTabs = listOf(
                    Triple("Home", Icons.Default.Home, "HOME"),
                    Triple("Register", Icons.Default.AppRegistration, "ROSTER ENVELOPE"),
                    Triple("League", Icons.Default.EmojiEvents, "LEAGUE CENTRAL"),
                    Triple("Admin", Icons.Default.AdminPanelSettings, "SECURE KERNEL")
                )

                navigationTabs.forEach { (tabId, icon, label) ->
                    val isSelected = currentTab == tabId
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onTabSelected(tabId) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color(0xFF10C469) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        }
                        if (isSelected) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(2.dp)
                                    .background(Color(0xFFD4AF37))
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }

            // Website Switcher Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF0F2C1E), RoundedCornerShape(100.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFF10C469), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("LIVE WEB", color = Color(0xFF10C469), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onToggleMode(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF141414)),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("📱 APP", color = Color(0xFFD4AF37), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

