package com.example.ui.screens

import com.example.ui.components.WebFooter
import android.app.DatePickerDialog
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlayerRegistration
import com.example.ui.LeagueViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: LeagueViewModel,
    onRegistrationSuccess: (PlayerRegistration) -> Unit,
    modifier: Modifier = Modifier,
    isWebsiteMode: Boolean = false
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form Fields State
    var fullName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var phone by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var residentialArea by remember { mutableStateOf("") }
    
    var experienceYears by remember { mutableStateOf("2") }
    var preferredCueHand by remember { mutableStateOf("Right-Handed") }
    var previousTournament by remember { mutableStateOf("") }
    var skillLevel by remember { mutableStateOf("Intermediate") }
    
    var emergencyName by remember { mutableStateOf("") }
    var emergencyRelationship by remember { mutableStateOf("") }
    var emergencyPhone by remember { mutableStateOf("") }
    
    // Custom simulated uploads
    var passportUri by remember { mutableStateOf<String?>(null) }
    var paymentProofUri by remember { mutableStateOf<String?>(null) }

    // Agreements Checkboxes
    var mediaConsent by remember { mutableStateOf(false) }
    var codeConduct by remember { mutableStateOf(false) }
    var liabilityDisclaimer by remember { mutableStateOf(false) }
    var playerConfirmation by remember { mutableStateOf(false) }

    // Dropdowns / Dialog triggers
    val genderOptions = listOf("Male", "Female", "Other")
    val cueHandOptions = listOf("Right-Handed", "Left-Handed", "Ambidextrous")
    val skillOptions = listOf("Beginner", "Intermediate", "Advanced")

    // Error list
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "OFFICIAL PLAYER REGISTRATION",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Complete the sections below to secure your ASC-ID and community slot.",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        if (validationError != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = "Error", tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = validationError ?: "", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // ====== SECTION 1: PERSONAL INFORMATION ======
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1. Personal Information",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name *") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname / Alias (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Date of Birth Button Picker
                OutlinedTextField(
                    value = dob,
                    onValueChange = { },
                    label = { Text("Date of Birth *") },
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR) - 20
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)
                            DatePickerDialog(context, { _, y, m, d ->
                                dob = "$y-${m + 1}-$d"
                            }, year, month, day).show()
                        }) {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Select Date", tint = Color(0xFF00A651))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Gender Selection
                Text(text = "Gender *", color = Color.Gray, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    genderOptions.forEach { text ->
                        Row(
                            modifier = Modifier
                                .clickable { gender = text }
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (gender == text),
                                onClick = { gender = text },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00A651))
                            )
                            Text(text = text, color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp Number *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = residentialArea,
                    onValueChange = { residentialArea = it },
                    label = { Text("Residential Area * (e.g. GRA, Tanke, Fate, Adewole)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Popular areas in Ilorin (Tap to select):", color = Color.Gray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.foundation.lazy.LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(listOf("GRA", "Tanke", "Fate", "Adewole", "Sango", "Gaa Akanbi", "Sabo Oke", "Sawmill")) { area ->
                        val isSelected = residentialArea.trim().equals(area, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFF00A651) else Color.White.copy(alpha = 0.05f),
                                    RoundedCornerShape(4.dp)
                                )
                                .border(
                                    0.5.dp,
                                    if (isSelected) Color(0xFF00A651) else Color.Gray.copy(alpha = 0.3f),
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { residentialArea = area }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(text = area, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ====== SECTION 2: PLAYER INFORMATION ======
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.SportsBasketball, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "2. Player Information / Cue Stats",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = experienceYears,
                    onValueChange = { experienceYears = it },
                    label = { Text("Years of Experience *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Cue Hand Selection
                Text(text = "Preferred Cue Hand *", color = Color.Gray, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    cueHandOptions.forEach { hand ->
                        Row(
                            modifier = Modifier
                                .clickable { preferredCueHand = hand }
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (preferredCueHand == hand),
                                onClick = { preferredCueHand = hand },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00A651))
                            )
                            Text(text = hand, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Skill level selector
                Text(text = "Skill Level *", color = Color.Gray, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    skillOptions.forEach { skl ->
                        ElevatedFilterChip(
                            selected = (skillLevel == skl),
                            onClick = { skillLevel = skl },
                            label = { Text(skl) },
                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                selectedContainerColor = Color(0xFF00A651),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF1E1E1E),
                                labelColor = Color.Gray
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = previousTournament,
                    onValueChange = { previousTournament = it },
                    label = { Text("Previous Tournament Participation / Top Break") },
                    placeholder = { Text("e.g. Ilorin Open 2025 (Top rank 8) or None") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        }

        // ====== SECTION 3: EMERGENCY CONTACT ======
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3. Emergency Contact",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = emergencyName,
                    onValueChange = { emergencyName = it },
                    label = { Text("Name of Contact *") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = emergencyRelationship,
                    onValueChange = { emergencyRelationship = it },
                    label = { Text("Relationship * (e.g. Spouse, Brother, Friend)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = emergencyPhone,
                    onValueChange = { emergencyPhone = it },
                    label = { Text("Emergency Contact Phone Number *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00A651),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // ====== SECTION 4: PHOTO & PAYMENT PROOF ======
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Attachment, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "4. Photo & Receipt Attachments",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A tournament registration fee of ₦25,000 applies. You can complete your secure online checkout directly via our fiduciary partner, Paystack. After payment is verified, your portal roster slot will be dynamically reserved.",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // PAYSTACK CHECKOUT INTERACTION PLACEHOLDER
                Button(
                    onClick = {
                        // Dynamically generate paystack invoice proof and verify payment
                        paymentProofUri = "https://paystack.com/receipt/ASCL-TXN-SUCCESS-MOCK"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF09A5DB) // Paystack Teal-Blue color
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "Paystack Card",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (paymentProofUri == null) "PAY WITH PAYSTACK" else "✓ PAID WITH PAYSTACK",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // PASSPORT PHOTO UPLOADER
                Text(text = "Passport Photograph *", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (passportUri != null) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.DarkGray)
                                .border(1.dp, Color(0xFF00A651), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👤", fontSize = 32.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black)
                                .border(1.dp, Color.Gray.copy(0.3f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            // Assign beautiful mock passport photo URL so the app has high fidelity visuals
                            passportUri = "https://example.com/mock-passport-player.jpg"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E1E1E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (passportUri == null) "SELECT PHOTO" else "SELECT DIFFERENT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // PAYMENT RECEIPT UPLOADER
                Text(text = "Proof of Payment Receipt *", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (paymentProofUri != null) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.DarkGray)
                                .border(1.dp, Color(0xFF00A651), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📄", fontSize = 32.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black)
                                .border(1.dp, Color.Gray.copy(0.3f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            // Assign beautiful mock payment proof Uri
                            paymentProofUri = "https://example.com/mock-proof-ascl.png"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E1E1E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (paymentProofUri == null) "ATTACH RECEIPT" else "ATTACH DIFFERENT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ====== SECTION 5: AGREEMENTS ======
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.FactCheck, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "5. Rules & Declarations",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                AgreementRow(
                    checked = mediaConsent,
                    onCheckedChange = { mediaConsent = it },
                    text = "I grant media consent to ASCL to record our tournament cues and broadcast matches live."
                )

                AgreementRow(
                    checked = codeConduct,
                    onCheckedChange = { codeConduct = it },
                    text = "I agree to adhere to the EPBSA snooker laws and ASCL official code of competitive conduct."
                )

                AgreementRow(
                    checked = liabilityDisclaimer,
                    onCheckedChange = { liabilityDisclaimer = it },
                    text = "I release ASCL and sponsors of liability for occurrences during tournament days."
                )

                AgreementRow(
                    checked = playerConfirmation,
                    onCheckedChange = { playerConfirmation = it },
                    text = "I confirm that all information provided is accurate and true to the best of my knowledge."
                )
            }
        }

        // Submit Button
        val isFormFilledAndAgreed = fullName.isNotBlank() &&
                dob.isNotBlank() &&
                phone.isNotBlank() &&
                whatsapp.isNotBlank() &&
                email.isNotBlank() &&
                residentialArea.isNotBlank() &&
                emergencyName.isNotBlank() &&
                emergencyRelationship.isNotBlank() &&
                emergencyPhone.isNotBlank() &&
                passportUri != null &&
                paymentProofUri != null &&
                mediaConsent && codeConduct && liabilityDisclaimer && playerConfirmation

        Button(
            onClick = {
                if (!isFormFilledAndAgreed) {
                    if (passportUri == null || paymentProofUri == null) {
                        validationError = "Please select/simulate the passport photo and payment receipt attachments."
                    } else if (!mediaConsent || !codeConduct || !liabilityDisclaimer || !playerConfirmation) {
                        validationError = "You must read and check all rules & declarations checkboxes to submit."
                    } else {
                        validationError = "Please fill in all fields marked with an asterisk (*)."
                    }
                    return@Button
                }

                validationError = null
                
                // Submit to view model
                viewModel.registerPlayer(
                    fullName = fullName,
                    nickname = nickname.ifBlank { null },
                    dob = dob,
                    gender = gender,
                    phone = phone,
                    whatsapp = whatsapp,
                    email = email,
                    residentialArea = residentialArea,
                    experienceYears = experienceYears.toIntOrNull() ?: 0,
                    preferredCueHand = preferredCueHand,
                    previousTournament = previousTournament,
                    skillLevel = skillLevel,
                    emergencyName = emergencyName,
                    emergencyRelationship = emergencyRelationship,
                    emergencyPhone = emergencyPhone,
                    passportPhotoUri = passportUri,
                    paymentProofUri = paymentProofUri,
                    onSuccess = { playerReg ->
                        onRegistrationSuccess(playerReg)
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilledAndAgreed) Color(0xFF00A651) else Color.DarkGray,
                contentColor = if (isFormFilledAndAgreed) Color.White else Color.Gray
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    1.dp, 
                    if (isFormFilledAndAgreed) Color(0xFF10C469) else Color.DarkGray, 
                    RoundedCornerShape(8.dp)
                )
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SUBMIT ASCL REGISTRATION",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        if (isWebsiteMode) {
            WebFooter()
        }
    }
}

@Composable
fun AgreementRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00A651))
        )
        Text(
            text = text,
            color = Color.LightGray,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            modifier = Modifier.padding(start = 6.dp, top = 2.dp)
        )
    }
}
