package com.example.studentcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcard.ui.theme.StudentCardTheme

private val Ink = Color(0xFF1A1A1A)
private val CardBackground = Color(0xA9A9A9)
private val StudentInfo = listOf(
    "Ngày sinh/ DOB" to "17/06/2006",
    "MSSV/ Student ID" to "202417108",
    "Khoá/ Intake" to "69",
    "Giá trị đến/ Valid until" to "30/07/2028",
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        StudentCard()
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCard(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentColor provides Ink) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CardBackground)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_hust),
                    contentDescription = "HUST Logo",
                    modifier = Modifier
                        .height(92.dp),
                    contentScale = ContentScale.FillHeight
                )
                Column {
                    Text("ĐẠI HỌC", fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 20.sp)
                    Text("BÁCH KHOA HÀ NỘI", fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 20.sp)
                    Text("HANOI UNIVERSITY", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 12.sp)
                    Text("OF SCIENCE AND TECHNOLOGY", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 12.sp)
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                thickness = 2.dp,
                color = Color.Red
            )

            Text("THẺ SINH VIÊN", fontWeight = FontWeight.Bold, fontSize = 25.sp)
            Text("Student ID Card", fontSize = 20.sp)

            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Student Avatar",
                modifier = Modifier
                    .padding(top = 18.dp)
                    .size(width = 150.dp, height = 150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "ĐỖ HUY DU",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 18.dp)
            )
            Text(
                text = "TRƯỜNG CÔNG NGHỆ THÔNG TIN VÀ TRUYỀN THÔNG",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "School of Information and Communications Technology",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Label column sizes to the longest label, so every colon lands on the same x.
                Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                    // Watermark is the first child, so the two columns below draw on top of it.
                    Image(
                        painter = painterResource(R.drawable.logo_z_0),
                        contentDescription = null,
                        modifier = Modifier
                            .height(110.dp)
                            .alpha(0.12f)
                    )
                    Row {
                        Column(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StudentInfo.forEach { Text(it.first, fontSize = 13.sp) }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            StudentInfo.forEach { Text(": ${it.second}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
                        }
                    }
                }
                Image(
                    painter = painterResource(R.drawable.qr),
                    contentDescription = "QR code",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun StudentCardPreview() {
    StudentCardTheme {
        StudentCard(Modifier.padding(16.dp))
    }
}
