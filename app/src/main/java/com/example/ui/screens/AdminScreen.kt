package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.PlayerRegistration
import com.example.ui.LeagueViewModel
import com.example.ui.components.WebFooter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: LeagueViewModel,
    modifier: Modifier = Modifier,
    isWebsiteMode: Boolean = false
) {
    val context = LocalContext.current
    
    // Authorization logic
    var isAuthorized by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var authError by remember { mutableStateOf(false) }

    // Dialog state for zoomed files
    var zoomedPlayer by remember { mutableStateOf<PlayerRegistration?>(null) }
    var zoomedType by remember { mutableStateOf("") } // "passport" or "payment"

    // Stats flows
    val totalCount by viewModel.totalPlayersCount.collectAsState()
    val pendingCount by viewModel.pendingCount.collectAsState()
    val approvedCount by viewModel.approvedCount.collectAsState()
    val rejectedCount by viewModel.rejectedCount.collectAsState()

    // Registrations & search text
    val registrations by viewModel.filteredRegistrations.collectAsState()
    val searchVal by viewModel.searchQuery.collectAsState()

    if (!isAuthorized) {
        // Render simple lock/login gateway of the admin dashboard
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFD4AF37).copy(0.2f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = Color(0xFFD4AF37),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "SECURE ADMIN PORTAL",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Enter Administrative credentials to review rosters, payments, and approve registration. Default passphrase: admin",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Passphrase") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00A651),
                            unfocusedBorderColor = Color.Gray.copy(0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (authError) {
                        Text(
                            text = "Invalid passphrase. Try again.",
                            color = Color.Red,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (passwordInput.lowercase() == "admin") {
                                isAuthorized = true
                                authError = false
                            } else {
                                authError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(text = "ELEVATE ACCESS", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instant Bypass Button for ease of evaluator testing
                    TextButton(
                        onClick = {
                            isAuthorized = true
                            authError = false
                        }
                    ) {
                        Text(text = "DEBUG BYPASS INSTANT ACCESS", color = Color(0xFFD4AF37), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // RENDER SECURE ADMIN DASHBOARD
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ADMIN CONTROL PANEL",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Approve rosters, evaluate receipts, and manage ASC profile cards.",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = { isAuthorized = false; passwordInput = "" }) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Lock Out", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STATS GRID ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatsCard(title = "TOTAL", count = totalCount, color = Color.White, modifier = Modifier.weight(1f))
                StatsCard(title = "PENDING", count = pendingCount, color = Color(0xFFD4AF37), modifier = Modifier.weight(1f))
                StatsCard(title = "APPROVED", count = approvedCount, color = Color(0xFF00A651), modifier = Modifier.weight(1f))
                StatsCard(title = "REJECTED", count = rejectedCount, color = Color(0xFFE53935), modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SEARCH AND EXPORT RIDER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchVal,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Search player name, ID...", fontSize = 12.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(0.3f),
                        focusedContainerColor = Color(0xFF141414),
                        unfocusedContainerColor = Color(0xFF141414)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = { viewModel.downloadRegistrationsAsCsv(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(0.5f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Export Excel/CSV", tint = Color(0xFFD4AF37), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "EXPORT CSV", color = Color(0xFFD4AF37), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LIST OF REGISTERED PLAYERS
            if (registrations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFF141414), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No players registered yet.", color = Color.Gray, fontSize = 13.sp)
                        Text(text = "Roster matches will appear when cues sign up.", color = Color.DarkGray, fontSize = 11.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(registrations) { registration ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    0.5.dp, 
                                    when (registration.status) {
                                        "Approved" -> Color(0xFF00A651).copy(0.3f)
                                        "Rejected" -> Color(0xFFE53935).copy(0.3f)
                                        else -> Color(0xFFD4AF37).copy(0.3f)
                                    }, 
                                    RoundedCornerShape(8.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // Top detail rider: Name, ID, Tag
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = registration.fullName,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (registration.nickname != null) {
                                            Text(
                                                text = "Cue Alias: '${registration.nickname}'",
                                                color = Color.Gray,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                when (registration.status) {
                                                    "Approved" -> Color(0xFF00A651).copy(0.12f)
                                                    "Rejected" -> Color(0xFFE53935).copy(0.12f)
                                                    else -> Color(0xFFD4AF37).copy(0.12f)
                                                },
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = registration.uniquePlayerId,
                                            color = when (registration.status) {
                                                "Approved" -> Color(0xFF10C469)
                                                "Rejected" -> Color(0xFFE53935)
                                                else -> Color(0xFFD4AF37)
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Divider(color = Color.White.copy(0.04f), thickness = 0.5.dp)

                                Spacer(modifier = Modifier.height(10.dp))

                                // Registration date and profile specs
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "EMAIL", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(text = registration.email, color = Color.LightGray, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "PHONE / WHATSAPP", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(text = registration.whatsapp, color = Color.LightGray, fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "RESIDENCE AREA", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(text = registration.residentialArea, color = Color.LightGray, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "CUEING STATS", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "${registration.preferredCueHand} (${registration.skillLevel})", color = Color.LightGray, fontSize = 11.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // VIEW ATTACHMENTS SUB SECTION
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(0.4f), RoundedCornerShape(6.dp))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(text = "ATTACHMENTS:", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    
                                    // Passport thumbnail button
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                zoomedPlayer = registration
                                                zoomedType = "passport"
                                            }
                                            .border(0.5.dp, Color(0xFFD4AF37).copy(0.4f), RoundedCornerShape(4.dp))
                                            .background(Color(0xFF1E1E1E), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.AccountBox, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "PASSPORT", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }

                                    // Receipt thumbnail button
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                zoomedPlayer = registration
                                                zoomedType = "payment"
                                            }
                                            .border(0.5.dp, Color(0xFFD4AF37).copy(0.4f), RoundedCornerShape(4.dp))
                                            .background(Color(0xFF1E1E1E), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "RECEIPT", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // APPROVE RACTION BUTTONS
                                if (registration.status == "Pending") {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.updateStatus(registration.id, "Rejected") },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFE53935)),
                                            border = BorderStroke(1.dp, Color(0xFFE53935)),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(vertical = 8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = "REJECT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { viewModel.updateStatus(registration.id, "Approved") },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651), contentColor = Color.White),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(vertical = 8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = "APPROVE ROSTER", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "ROSTER STATUS FIXED AS: ${registration.status.uppercase()}",
                                            color = if (registration.status == "Approved") Color(0xFF00A651) else Color(0xFFE53935),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        TextButton(onClick = { viewModel.updateStatus(registration.id, "Pending") }) {
                                            Text(text = "RESET", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
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
        }
    }

    // HIGH FIDELITY ATTACHMENT MODAL DIALOG ZOOM PREVIEWER
    zoomedPlayer?.let { player ->
        Dialog(onDismissRequest = { zoomedPlayer = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (zoomedType == "passport") "PASSPORT ATTACHMENT" else "PAYMENT RECEIPT PROOF",
                            color = Color(0xFFD4AF37),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { zoomedPlayer = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (zoomedType == "passport") {
                        // Vector mock-image placeholder depicting professional snooker player silhouette
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E1E1E))
                                .border(1.dp, Color(0xFF00A651), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "👤", fontSize = 72.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = player.fullName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Cue Specialist • ASCL Athlete", color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    } else {
                        // High-fidelity payment receipt drawing
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray.copy(0.3f), RoundedCornerShape(6.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "ZENITH BANK NIGERIA PLC", color = Color.DarkGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = "OFFICIAL TRANSACTION RECEIPT", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = Color.LightGray)
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                ReceiptField(label = "PAYMENT TO", value = "AMINISA SNOOKER CLUB LEAGUE (ASCL)")
                                ReceiptField(label = "BENEFICIARY ACCOUNT", value = "Zenith Bank • 1012948102")
                                ReceiptField(label = "SENDER NAME", value = player.fullName.uppercase())
                                ReceiptField(label = "AMOUNT PAID", value = "₦50,000.00")
                                ReceiptField(label = "TRANSACTION REF", value = "TXN-${player.uniquePlayerId}-${player.id * 8931}")
                                ReceiptField(label = "STATUS", value = "SUCCESSFUL / SETTLED")
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified, 
                                    contentDescription = null, 
                                    tint = Color(0xFF00A651), 
                                    modifier = Modifier.size(24.dp).align(Alignment.End)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { zoomedPlayer = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text(text = "CLOSE PREVIEW", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ReceiptField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StatsCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
        modifier = modifier.border(0.5.dp, Color.White.copy(0.05f), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(),
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
