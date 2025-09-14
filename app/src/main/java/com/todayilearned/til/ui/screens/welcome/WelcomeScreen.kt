package com.todayilearned.til.ui.screens.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(
    onGoogleLoginClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    // Responsive layout calculations
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val topPadding = min(screenHeight * 0.18f, 120.dp)
    val cardMaxWidth = min(screenWidth * 0.9f, 400.dp)

    // Animation control
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.surface,
                        colors.background
                    )
                )
            )
    ) {
        // Subtle decorative elements
        DecorativeElements(colors)

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = topPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and branding
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(initialAlpha = 0.6f, animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(300))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .padding(8.dp),
                        tint = colors.primary
                    )
                    Text(
                        text = "TIL",
                        style = typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        letterSpacing = (-0.5).sp
                    )
                }
            }

            // Main content card
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(initialAlpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(max = cardMaxWidth)
                        .animateContentSize(
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ),
                    shape = RoundedCornerShape(28.dp),
                    color = colors.surface,
                    shadowElevation = 8.dp,
                    tonalElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .animateContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ContentHeader(colors, typography)

                        Spacer(Modifier.height(32.dp))

                        AnimatedContent(
                            targetState = contentVisible,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) with
                                        fadeOut(animationSpec = tween(200))
                            }
                        ) { visible ->
                            if (visible) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    GoogleSignInButton(onGoogleLoginClick, colors)
                                    SkipButton(onSkipClick, colors)
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        Text(
                            text = "Continue to unlock social features and sync your learning journey across devices",
                            style = typography.bodyMedium,
                            color = colors.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            // Footer
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Privacy Policy • Terms of Service",
                    style = typography.labelMedium,
                    color = colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 16.dp)
                        .clickable { showPrivacyDialog = true }
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Privacy Dialog
    PrivacyPolicyDialog(
        show = showPrivacyDialog,
        onDismiss = { showPrivacyDialog = false },
        colors = colors,
        typography = typography
    )
}

@Composable
private fun DecorativeElements(colors: ColorScheme) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Subtle background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.03f),
                            Color.Transparent
                        ),
                        radius = 1.5f
                    )
                )
        )

        // Decorative shapes with intentional placement
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset((-40).dp, (-30).dp)
                .blur(60.dp)
                .background(
                    Brush.sweepGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.1f),
                            colors.secondary.copy(alpha = 0.05f),
                            colors.tertiary.copy(alpha = 0.05f)
                        )
                    ),
                    RoundedCornerShape(110.dp)
                )
        )

        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(200.dp, 500.dp)
                .blur(40.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.secondary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(90.dp)
                )
        )
    }
}

@Composable
private fun ContentHeader(colors: ColorScheme, typography: Typography) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Text(
            text = "Welcome to TIL",
            style = typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Transform the way you learn by capturing knowledge in bite-sized pieces",
            style = typography.bodyLarge,
            color = colors.onSurface.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    colors: ColorScheme
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    ElevatedButton(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 5.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            tint = Color(0xFFDB4437), // Google red
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Continue with Google",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SkipButton(
    onClick: () -> Unit,
    colors: ColorScheme
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Explore without signing in",
            style = MaterialTheme.typography.titleMedium,
            color = colors.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun PrivacyPolicyDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    colors: ColorScheme,
    typography: Typography
) {
    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = colors.surface,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .animateContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Your Privacy Matters",
                        style = typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "We never share your data without explicit permission. " +
                                "Your learning journey is stored securely and only synced " +
                                "when you're signed in. You maintain full control over your information."+
                        "(Also broke to even store them anyway)",
                        style = typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        )
                    ) {
                        Text(
                            text = "Understood",
                            style = typography.titleMedium,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}