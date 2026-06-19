package com.artisan.browser

import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Terminal
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark

import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import com.artisan.browser.ui.theme.BrowserTheme
import com.artisan.browser.utils.DampedDragAnimation
import com.artisan.browser.utils.ripple
import com.kyant.shapes.RoundedRectangle
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import com.kyant.shapes.Capsule
import kotlinx.coroutines.flow.collectLatest
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebResponse

data class BrowserSettings(
    val blurIntensity: Float = 1f,
    val tintIntensity: Float = 1f
)

val LocalBrowserSettings = compositionLocalOf { BrowserSettings() }

class BrowserTab(
    val session: GeckoSession,
    initialUrl: String = "https://www.google.com",
    val isPrivate: Boolean = false
) {
    var title by mutableStateOf("Loading...")
    var url by mutableStateOf(initialUrl)
    var progress by mutableFloatStateOf(0f)
    var isLoading by mutableStateOf(false)
    var canGoBack by mutableStateOf(false)
    var canGoForward by mutableStateOf(false)
    var themeColor by mutableStateOf<Color?>(null)
    var bottomColor by mutableStateOf<Color?>(null)
    var snapshot by mutableStateOf<ImageBitmap?>(null)
    var scrollY by mutableIntStateOf(0)
    var maxScrollY by mutableIntStateOf(0)
    var isDesktopMode by mutableStateOf(false)
    var isEditMode by mutableStateOf(false)
    val consoleLogs = mutableStateListOf<String>()
}

data class HistoryEntry(val title: String, val url: String, val timestamp: Long = System.currentTimeMillis())
data class Bookmark(val title: String, val url: String)

data class DownloadItem(
    val id: Long,
    val title: String,
    val url: String,
    val mimeType: String?,
    val status: Int = DownloadManager.STATUS_PENDING,
    val progress: Float = 0f,
    val totalSize: Long = 0,
    val downloadedSize: Long = 0,
    val localUri: String? = null
)

enum class Screen { Browser, Tabs, History, Bookmarks, Settings, Downloads, DevConsole }

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrowserTheme {
                val context = LocalContext.current
                var blurIntensity by remember { mutableFloatStateOf(1f) }
                var tintIntensity by remember { mutableFloatStateOf(1f) }
                
                val settings = remember(blurIntensity, tintIntensity) {
                    BrowserSettings(blurIntensity, tintIntensity)
                }

                CompositionLocalProvider(LocalBrowserSettings provides settings) {
                    val backgroundColor = MaterialTheme.colorScheme.background
                    
                    val backdrop = rememberLayerBackdrop {
                        drawRect(backgroundColor)
                        drawContent()
                    }

                    val focusManager = LocalFocusManager.current
                    val keyboardController = LocalSoftwareKeyboardController.current

                    val geckoRuntime = remember { GeckoRuntime.getDefault(context) }
                    val tabs = remember { mutableStateListOf<BrowserTab>() }
                    var currentTabIndex by remember { mutableIntStateOf(0) }
                    val bookmarks = remember { mutableStateListOf<Bookmark>() }
                    val history = remember { mutableStateListOf<HistoryEntry>() }
                    val downloads = remember { mutableStateListOf<DownloadItem>() }
                    var isDeveloperMode by remember { mutableStateOf(false) }

                    val downloadManager = remember { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

                    LaunchedEffect(Unit) {
                        val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
                        isDeveloperMode = prefs.getBoolean("developer_mode", false)
                        blurIntensity = prefs.getFloat("blur_intensity", 1f)
                        tintIntensity = prefs.getFloat("tint_intensity", 1f)
                        history.addAll(loadHistory(context))
                        bookmarks.addAll(loadBookmarks(context))
                    }

                LaunchedEffect(Unit) {
                    while (true) {
                        for (i in downloads.indices) {
                            val item = downloads.getOrNull(i) ?: continue
                            if (item.status == DownloadManager.STATUS_RUNNING || item.status == DownloadManager.STATUS_PENDING || item.status == DownloadManager.STATUS_PAUSED) {
                                val query = DownloadManager.Query().setFilterById(item.id)
                                val cursor = downloadManager.query(query)
                                if (cursor != null && cursor.moveToFirst()) {
                                    val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                    val downloadedIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                    val totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                                    val localUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                                    
                                    var status = item.status
                                    var downloadedSize = item.downloadedSize
                                    var totalSize = item.totalSize
                                    var localUri = item.localUri
                                    
                                    if (statusIdx != -1) status = cursor.getInt(statusIdx)
                                    if (downloadedIdx != -1) downloadedSize = cursor.getLong(downloadedIdx)
                                    if (totalIdx != -1) totalSize = cursor.getLong(totalIdx)
                                    if (localUriIdx != -1) localUri = cursor.getString(localUriIdx)
                                    
                                    var progress = 0f
                                    if (totalSize > 0) {
                                        progress = downloadedSize.toFloat() / totalSize
                                    }
                                    
                                    val updatedItem = item.copy(
                                        status = status,
                                        downloadedSize = downloadedSize,
                                        totalSize = totalSize,
                                        localUri = localUri,
                                        progress = progress
                                    )
                                    if (updatedItem != item) {
                                        downloads[i] = updatedItem
                                    }
                                }
                                cursor?.close()
                            }
                        }
                        kotlinx.coroutines.delay(1000)
                    }
                }

                var currentScreen by remember { mutableStateOf(Screen.Browser) }

                fun createTab(url: String = "https://www.google.com", isPrivate: Boolean = false): BrowserTab {
                    val session = GeckoSession()
                    val tab = BrowserTab(session, url, isPrivate)
                    session.open(geckoRuntime)
                    
                    session.navigationDelegate = object : GeckoSession.NavigationDelegate {
                        override fun onLocationChange(session: GeckoSession, url: String?, permissions: List<GeckoSession.PermissionDelegate.ContentPermission>, hasUserGesture: Boolean) {
                            url?.let {
                                tab.url = it
                                if (!tab.isPrivate) {
                                    if (history.firstOrNull()?.url != it) {
                                        history.add(0, HistoryEntry(tab.title, it))
                                        if (history.size > 100) history.removeAt(history.size - 1)
                                        saveHistory(context, history)
                                    }
                                }
                            }
                        }
                    }

                    session.historyDelegate = object : GeckoSession.HistoryDelegate {
                        override fun onHistoryStateChange(session: GeckoSession, historyList: GeckoSession.HistoryDelegate.HistoryList) {
                            tab.canGoBack = historyList.currentIndex > 0
                            tab.canGoForward = historyList.currentIndex < historyList.size - 1
                        }
                    }

                    session.progressDelegate = object : GeckoSession.ProgressDelegate {
                        override fun onPageStart(session: GeckoSession, url: String) { 
                            tab.isLoading = true 
                            tab.themeColor = null
                        }
                        override fun onPageStop(session: GeckoSession, success: Boolean) { 
                            tab.isLoading = false 
                            if (success) {
                                session.loadUri("javascript:(function(){ " +
                                    "const getTheme = () => document.querySelector('meta[name=\"theme-color\"]')?.content || ''; " +
                                    "const getBottom = () => window.getComputedStyle(document.body).backgroundColor; " +
                                    "const getHeight = () => Math.max(0, document.documentElement.scrollHeight - window.innerHeight); " +
                                    "window.alert('theme-color:' + getTheme()); " +
                                    "window.alert('bottom-color:' + getBottom()); " +
                                    "window.alert('scroll-height:' + getHeight()); " +
                                    "const observer = new ResizeObserver(() => { " +
                                    "  window.alert('scroll-height:' + getHeight()); " +
                                    "}); " +
                                    "observer.observe(document.documentElement); " +
                                    "})()")
                            }
                        }
                        override fun onProgressChange(session: GeckoSession, progress: Int) { tab.progress = progress / 100f }
                    }

                    session.promptDelegate = object : GeckoSession.PromptDelegate {
                        override fun onAlertPrompt(session: GeckoSession, prompt: GeckoSession.PromptDelegate.AlertPrompt): org.mozilla.geckoview.GeckoResult<GeckoSession.PromptDelegate.PromptResponse>? {
                            val message = prompt.message
                            if (message?.startsWith("theme-color:") == true) {
                                val colorStr = message.substringAfter("theme-color:")
                                if (colorStr.isNotEmpty()) {
                                    try {
                                        tab.themeColor = Color(colorStr.toColorInt())
                                    } catch (_: Exception) {}
                                }
                                return org.mozilla.geckoview.GeckoResult.fromValue(prompt.dismiss())
                            }
                            if (message?.startsWith("bottom-color:") == true) {
                                val colorStr = message.substringAfter("bottom-color:")
                                if (colorStr.isNotEmpty()) {
                                    tab.bottomColor = parseCssColor(colorStr)
                                }
                                return org.mozilla.geckoview.GeckoResult.fromValue(prompt.dismiss())
                            }
                            if (message?.startsWith("scroll-height:") == true) {
                                val heightStr = message.substringAfter("scroll-height:")
                                tab.maxScrollY = heightStr.toDoubleOrNull()?.toInt() ?: 0
                                return org.mozilla.geckoview.GeckoResult.fromValue(prompt.dismiss())
                            }
                            if (message?.startsWith("console-result:") == true) {
                                tab.consoleLogs.add("-> " + message.substringAfter("console-result:"))
                                return org.mozilla.geckoview.GeckoResult.fromValue(prompt.dismiss())
                            }
                            if (message?.startsWith("console-error:") == true) {
                                tab.consoleLogs.add("Error: " + message.substringAfter("console-error:"))
                                return org.mozilla.geckoview.GeckoResult.fromValue(prompt.dismiss())
                            }
                            return null 
                        }
                    }

                    session.scrollDelegate = object : GeckoSession.ScrollDelegate {
                        override fun onScrollChanged(session: GeckoSession, scrollX: Int, scrollY: Int) {
                            tab.scrollY = scrollY
                        }
                    }

                    session.contentDelegate = object : GeckoSession.ContentDelegate {
                        override fun onTitleChange(session: GeckoSession, title: String?) {
                            title?.let { tab.title = it }
                        }

                        override fun onExternalResponse(session: GeckoSession, response: WebResponse) {
                            val uri = Uri.parse(response.uri)
                            val fileName = URLUtil.guessFileName(response.uri, response.headers["Content-Disposition"], response.headers["Content-Type"])
                            val downloadRequest = DownloadManager.Request(uri).apply {
                                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                                setTitle(fileName)
                            }
                            val id = downloadManager.enqueue(downloadRequest)
                            downloads.add(0, DownloadItem(
                                id = id,
                                title = fileName,
                                url = response.uri,
                                mimeType = response.headers["Content-Type"]
                            ))
                            Toast.makeText(context, "Starting download: $fileName", Toast.LENGTH_SHORT).show()
                        }
                    }

                    session.loadUri(url)
                    return tab
                }

                if (tabs.isEmpty()) {
                    tabs.add(createTab())
                }

                val currentTab = tabs.getOrNull(currentTabIndex) ?: tabs.first()
                var urlValue by remember(currentTab) { 
                    mutableStateOf(TextFieldValue(currentTab.url)) 
                }
                var isEditingUrl by remember { mutableStateOf(false) }
                var showMenu by remember { mutableStateOf(false) }
                
                LaunchedEffect(isEditingUrl) {
                    if (isEditingUrl) {
                        urlValue = urlValue.copy(
                            selection = TextRange(0, urlValue.text.length)
                        )
                    }
                }
                
                LaunchedEffect(currentTab.url) {
                    if (!isEditingUrl) {
                        urlValue = TextFieldValue(currentTab.url)
                    }
                }

                var activeGeckoView by remember { mutableStateOf<GeckoView?>(null) }

                fun captureCurrentTab() {
                    val view = activeGeckoView ?: return
                    val tab = currentTab
                    if (view.width > 0 && view.height > 0) {
                        try {
                            val bitmap = createBitmap(view.width, view.height)
                            val canvas = Canvas(bitmap)
                            view.draw(canvas)
                            tab.snapshot = bitmap.asImageBitmap()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                val view = LocalView.current
                val window = (context as MainActivity).window
                SideEffect {
                    val isLight = currentTab.themeColor?.let { it.luminance() > 0.5f } 
                                 ?: (backgroundColor.luminance() > 0.5f)
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLight
                }

                BackHandler(enabled = true) {
                    when {
                        currentScreen != Screen.Browser -> currentScreen = Screen.Browser
                        currentTab.canGoBack -> currentTab.session.goBack()
                        tabs.size > 1 -> {
                            val toRemove = currentTabIndex
                            if (currentTabIndex > 0) currentTabIndex--
                            tabs[toRemove].session.close()
                            tabs.removeAt(toRemove)
                        }
                        else -> {
                            (context as? MainActivity)?.finish()
                        }
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    val bottomSpacer by animateDpAsState(
                        targetValue = if (currentTab.maxScrollY > 0 && currentTab.scrollY >= currentTab.maxScrollY - 10) 80.dp else 0.dp,
                        animationSpec = tween(500),
                        label = "BottomSpacer"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                compositingStrategy = CompositingStrategy.Offscreen,
                                alpha = 0.99f
                            )
                            .layerBackdrop(backdrop)
                            .background(currentTab.bottomColor ?: Color.Transparent)
                    ) {
                        AndroidView(
                            factory = {
                                GeckoView(context).apply {
                                    setViewBackend(GeckoView.BACKEND_TEXTURE_VIEW)
                                    setSession(currentTab.session)
                                    activeGeckoView = this
                                }
                            },
                            update = { view ->
                                if (view.session != currentTab.session) {
                                    view.setSession(currentTab.session)
                                    activeGeckoView = view
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .padding(top = 12.dp)
                                .padding(bottom = bottomSpacer)
                        )
                    }

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { androidx.compose.ui.graphics.RectangleShape },
                                effects = {
                                    vibrancy()
                                    blur(15.dp.toPx() * settings.blurIntensity)
                                },
                                onDrawSurface = {
                                    val themeColor = currentTab.themeColor ?: Color.Transparent
                                    drawRect(themeColor.copy(alpha = (0.8f * settings.tintIntensity).coerceIn(0f, 1f)))
                                }
                            )
                    )

                    Box(Modifier.fillMaxSize()) {
                        val progressBarBrush = remember {
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2196F3), Color(0xFFE91E63))
                            )
                        }
                        
                        Column(Modifier.align(Alignment.TopStart).statusBarsPadding()) {
                            if (currentTab.isLoading && currentTab.progress > 0f && currentTab.progress < 1f) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .graphicsLayer {
                                                scaleX = currentTab.progress
                                                transformOrigin = TransformOrigin(0f, 0f)
                                            }
                                            .background(progressBarBrush)
                                    )
                                }
                            }
                            
                            val activeDownload = downloads.firstOrNull { it.status == DownloadManager.STATUS_RUNNING }
                            if (activeDownload != null) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .graphicsLayer {
                                                scaleX = activeDownload.progress
                                                transformOrigin = TransformOrigin(0f, 0f)
                                            }
                                            .background(Color(0xFF4CAF50))
                                    )
                                }
                            }
                        }

                        key(settings.blurIntensity, settings.tintIntensity) {
                        Row(
                            Modifier
                                .padding(horizontal = 4.dp)
                                .safeContentPadding()
                                .padding(bottom = if (WindowInsets.isImeVisible) 8.dp else 0.dp)
                                .height(48.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier
                                    .aspectRatio(1f)
                                    .fillMaxHeight()
                                    .drawBackdrop(
                                        backdrop = backdrop,
                                        shape = { CircleShape },
                                        effects = {
                                            vibrancy()
                                            blur(1.dp.toPx() * settings.blurIntensity)
                                            lens(
                                                refractionHeight = 12.dp.toPx(),
                                                refractionAmount = 24.dp.toPx(),
                                                depthEffect = true,
                                                chromaticAberration = true
                                            )
                                        },
                                        onDrawSurface = {
                                            drawRect(Color.White.copy(alpha = (0.45f * settings.tintIntensity).coerceIn(0f, 1f)))
                                        },
                                    )
                                    .clip(CircleShape)
                                    .liquidClickable {
                                        currentTab.session.goBack()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = Color.Black.copy(alpha = 0.5f)
                                )
                            }

                            Box(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .drawBackdrop(
                                        backdrop = backdrop,
                                        shape = { RoundedRectangle(24.dp) },
                                        effects = {
                                            vibrancy()
                                            blur(2.dp.toPx() * settings.blurIntensity)
                                            lens(
                                                refractionHeight = 15.dp.toPx(),
                                                refractionAmount = 30.dp.toPx(),
                                                depthEffect = true,
                                                chromaticAberration = true
                                            )
                                        },
                                        onDrawSurface = {
                                            drawRect(Color.White.copy(alpha = (0.45f * settings.tintIntensity).coerceIn(0f, 1f)))
                                        },
                                    )
                                    .liquidClickable { /* Focus is handled by the TextField */ },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 12.dp, end = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    BasicTextField(
                                        value = urlValue,
                                        onValueChange = { urlValue = it },
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged { focusState ->
                                                isEditingUrl = focusState.isFocused
                                            },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = Color.Black.copy(alpha = 0.55f),
                                            fontSize = 16.sp,
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            )
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search,
                                            keyboardType = KeyboardType.Uri
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                keyboardController?.hide()
                                                focusManager.clearFocus()
                                                val resolved = resolveUrl(urlValue.text)
                                                urlValue = TextFieldValue(resolved)
                                                currentTab.session.loadUri(resolved)
                                            }
                                        ),
                                        cursorBrush = SolidColor(Color.Black.copy(alpha = 0.5f)),
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.CenterStart) {
                                                if (urlValue.text.isEmpty()) {
                                                    Text(
                                                        text = "Search",
                                                        color = Color.Black.copy(alpha = 0.3f),
                                                        fontSize = 16.sp
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        }
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .liquidClickable {
                                                if (currentTab.isLoading) {
                                                    currentTab.session.stop()
                                                } else {
                                                    currentTab.session.reload()
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (currentTab.isLoading) Icons.Default.Close else Icons.Default.Refresh,
                                            contentDescription = if (currentTab.isLoading) "Stop" else "Refresh",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.Black.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }

                            if (!showMenu) {
                                Box(
                                    Modifier
                                        .aspectRatio(1f)
                                        .fillMaxHeight()
                .drawBackdrop(
                                            backdrop = backdrop,
                                            shape = { CircleShape },
                                            effects = {
                                                vibrancy()
                                                blur(1.dp.toPx() * settings.blurIntensity)
                                                lens(
                                                    refractionHeight = 12.dp.toPx(),
                                                    refractionAmount = 24.dp.toPx(),
                                                    depthEffect = true,
                                                    chromaticAberration = true
                                                )
                                            },
                                            onDrawSurface = {
                                                drawRect(Color.White.copy(alpha = (0.45f * settings.tintIntensity).coerceIn(0f, 1f)))
                                            },
                                        )
                                        .clip(CircleShape)
                                        .liquidClickable { showMenu = !showMenu },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.MoreHoriz,
                                        contentDescription = "More",
                                        tint = Color.Black.copy(alpha = 0.5f)
                                    )
                                }
                            } else {
                                Spacer(Modifier.aspectRatio(1f).fillMaxHeight())
                            }
                        }
                        }

                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                            },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                Screen.Tabs -> TabsOverlay(
                                    backdrop = backdrop,
                                    tabs = tabs,
                                    currentIndex = currentTabIndex,
                                    onTabSelect = { index ->
                                        currentTabIndex = index
                                        currentScreen = Screen.Browser
                                    },
                                    onTabClose = { index ->
                                        if (tabs.size > 1) {
                                            tabs[index].session.close()
                                            tabs.removeAt(index)
                                            if (currentTabIndex >= tabs.size) currentTabIndex = tabs.size - 1
                                        }
                                    },
                                    onNewTab = {
                                        tabs.add(createTab())
                                        currentTabIndex = tabs.size - 1
                                        currentScreen = Screen.Browser
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.History -> HistoryPage(
                                    history = history,
                                    onNavigate = { url ->
                                        currentTab.session.loadUri(url)
                                        currentScreen = Screen.Browser
                                    },
                                    onClear = { 
                                        history.clear() 
                                        saveHistory(context, history)
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.Bookmarks -> BookmarksPage(
                                    bookmarks = bookmarks,
                                    onNavigate = { url ->
                                        currentTab.session.loadUri(url)
                                        currentScreen = Screen.Browser
                                    },
                                    onDelete = { 
                                        bookmarks.remove(it)
                                        saveBookmarks(context, bookmarks)
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.Settings -> SettingsPage(
                                    backdrop = backdrop,
                                    onBlurChange = {
                                        blurIntensity = it
                                        context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
                                            .edit().putFloat("blur_intensity", it).apply()
                                    },
                                    onTintChange = {
                                        tintIntensity = it
                                        context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
                                            .edit().putFloat("tint_intensity", it).apply()
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.Downloads -> DownloadsPage(
                                    downloads = downloads,
                                    onAction = { item: DownloadItem, action: String ->
                                        when (action) {
                                            "cancel" -> {
                                                downloadManager.remove(item.id)
                                                downloads.remove(item)
                                            }
                                            "copy" -> {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("Download URL", item.url))
                                                Toast.makeText(context, "URL Copied", Toast.LENGTH_SHORT).show()
                                            }
                                            "open" -> {
                                                item.localUri?.let { uriStr ->
                                                    try {
                                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                                            setDataAndType(Uri.parse(uriStr), item.mimeType)
                                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                        }
                                                        context.startActivity(Intent.createChooser(intent, "Open with"))
                                                    } catch (_: Exception) {
                                                        Toast.makeText(context, "Could not open file", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.DevConsole -> DevConsolePage(
                                    tab = currentTab,
                                    onExecute = { script ->
                                        val escapedScript = script.replace("'", "\\'")
                                        currentTab.session.loadUri("javascript:(function(){ " +
                                            "try { " +
                                            "  var res = eval('$escapedScript'); " +
                                            "  window.alert('console-result:' + res); " +
                                            "} catch(e) { " +
                                            "  window.alert('console-error:' + e.message); " +
                                            "} " +
                                            "})()")
                                    },
                                    onClose = { currentScreen = Screen.Browser }
                                )
                                Screen.Browser -> {}
                            }
                        }

                        var showFindInPage by remember { mutableStateOf(false) }
                        var findQuery by remember { mutableStateOf("") }
                        
                        AnimatedVisibility(
                            visible = showFindInPage,
                            enter = fadeIn() + scaleIn(initialScale = 0.95f),
                            exit = fadeOut() + scaleOut(targetScale = 0.95f),
                            modifier = Modifier.align(Alignment.TopCenter).statusBarsPadding().padding(8.dp)
                        ) {
                             Row(
                                 Modifier
                                     .fillMaxWidth()
                                     .height(56.dp)
                                     .drawBackdrop(
                                         backdrop = backdrop,
                                         shape = { RoundedCornerShape(28.dp) },
                                         effects = {
                                             vibrancy()
                                             blur(10.dp.toPx() * settings.blurIntensity)
                                         },
                                         onDrawSurface = { drawRect(Color.White.copy(alpha = (0.8f * settings.tintIntensity).coerceIn(0f, 1f))) }
                                     )
                                     .padding(horizontal = 16.dp),
                                 verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.spacedBy(12.dp)
                             ) {
                                 BasicTextField(
                                     value = findQuery,
                                     onValueChange = { 
                                         findQuery = it
                                         currentTab.session.loadUri("javascript:window.find('$it', false, false, true); void 0;")
                                     },
                                     modifier = Modifier.weight(1f),
                                     singleLine = true,
                                     decorationBox = { inner ->
                                         if (findQuery.isEmpty()) Text("Find in page...", color = Color.Gray)
                                         inner()
                                     }
                                 )
                                 Icon(
                                     Icons.Default.Close,
                                     "Close",
                                     Modifier.liquidClickable { 
                                         showFindInPage = false
                                         findQuery = ""
                                     }
                                 )
                             }
                        }

                        AnimatedVisibility(
                            visible = showMenu,
                            enter = fadeIn(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(250)),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { showMenu = false }
                            ) {
                                AnimatedVisibility(
                                    visible = showMenu,
                                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                                        initialScale = 0.8f,
                                        transformOrigin = TransformOrigin(1f, 1f),
                                        animationSpec = tween(300)
                                    ),
                                    exit = fadeOut(animationSpec = tween(250)) + scaleOut(
                                        targetScale = 0.8f,
                                        transformOrigin = TransformOrigin(1f, 1f),
                                        animationSpec = tween(250)
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(horizontal = 4.dp)
                                        .safeContentPadding()
                                        .padding(bottom = if (WindowInsets.isImeVisible) 8.dp else 0.dp)
                                        .width(220.dp)
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                     .drawBackdrop(
                                                backdrop = backdrop,
                                                shape = { RoundedRectangle(32.dp) },
                                                effects = {
                                                    vibrancy()
                                                    blur(5.dp.toPx() * settings.blurIntensity)
                                                    lens(
                                                        refractionHeight = 15.dp.toPx(),
                                                        refractionAmount = 30.dp.toPx(),
                                                        depthEffect = true,
                                                        chromaticAberration = true
                                                    )
                                                },
                                                onDrawSurface = {
                                                    drawRect(Color.White.copy(alpha = (0.35f * settings.tintIntensity).coerceIn(0f, 1f)))
                                                },
                                            )
                                            .clip(RoundedRectangle(32.dp))
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) { }
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                         Row(
                                             Modifier.fillMaxWidth(),
                                             horizontalArrangement = Arrangement.SpaceBetween
                                         ) {
                                             MenuHeaderItem(Icons.Default.Share, "Share") {
                                                 showMenu = false
                                                 val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                     type = "text/plain"
                                                     putExtra(Intent.EXTRA_TEXT, currentTab.url)
                                                 }
                                                 context.startActivity(Intent.createChooser(shareIntent, "Share URL"))
                                             }
                                             MenuHeaderItem(Icons.AutoMirrored.Filled.MenuBook, "Bookmarks") {
                                                 showMenu = false
                                                 currentScreen = Screen.Bookmarks
                                             }
                                             MenuHeaderItem(Icons.Default.ContentCopy, "Tabs") {
                                                 showMenu = false
                                                 captureCurrentTab()
                                                 currentScreen = Screen.Tabs
                                             }
                                         }

                                         if (currentTab.canGoForward) {
                                             HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                                             Row(
                                                 Modifier.fillMaxWidth(),
                                                 horizontalArrangement = Arrangement.SpaceBetween
                                             ) {
                                                  MenuHeaderItem(Icons.AutoMirrored.Filled.ArrowForward, "Forward") {
                                                                        showMenu = false
                                                                        currentTab.session.goForward()
                                                                    }
                                             }
                                         }

                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            MenuListItem(Icons.Default.DesktopWindows, if (currentTab.isDesktopMode) "Mobile Site" else "Desktop Site") {
                                                showMenu = false
                                                currentTab.isDesktopMode = !currentTab.isDesktopMode
                                                currentTab.session.settings.userAgentMode = if (currentTab.isDesktopMode) GeckoSessionSettings.USER_AGENT_MODE_DESKTOP else GeckoSessionSettings.USER_AGENT_MODE_MOBILE
                                                currentTab.session.reload()
                                            }
                                            MenuListItem(Icons.Default.FindInPage, "Find in Page") {
                                                showMenu = false
                                                showFindInPage = true
                                            }
                                            MenuListItem(Icons.Default.AddBox, "New Tab") {
                                                showMenu = false
                                                tabs.add(createTab())
                                                currentTabIndex = tabs.size - 1
                                            }
                                            MenuListItem(Icons.Default.AddBox, "New Private Tab") {
                                                showMenu = false
                                                tabs.add(createTab("https://duckduckgo.com", isPrivate = true))
                                                currentTabIndex = tabs.size - 1
                                            }
                                            MenuListItem(Icons.Default.History, "History") {
                                                showMenu = false
                                                currentScreen = Screen.History
                                            }
                                        }

                                        HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))

                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            if (isDeveloperMode) {
                                                MenuListItem(Icons.Default.Edit, if (currentTab.isEditMode) "Disable Edit Mode" else "Inspect (Edit Page)") {
                                                    showMenu = false
                                                    currentTab.isEditMode = !currentTab.isEditMode
                                                    val script = "javascript:document.body.contentEditable = ${currentTab.isEditMode}; void 0;"
                                                    currentTab.session.loadUri(script)
                                                }
                                                MenuListItem(Icons.Default.Terminal, "JS Console") {
                                                    showMenu = false
                                                    currentScreen = Screen.DevConsole
                                                }
                                            }
                                            MenuListItem(Icons.Default.Download, "Downloads") {
                                                showMenu = false
                                                currentScreen = Screen.Downloads
                                            }
                                            MenuListItem(Icons.Default.BookmarkAdd, "Add Bookmark") {
                                                showMenu = false
                                                bookmarks.add(Bookmark(currentTab.title, currentTab.url))
                                                saveBookmarks(context, bookmarks)
                                                Toast.makeText(context, "Bookmark Added", Toast.LENGTH_SHORT).show()
                                            }
                                            MenuListItem(Icons.Default.Settings, "Settings") {
                                                showMenu = false
                                                currentScreen = Screen.Settings
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
    }
}

@Composable
fun TabsOverlay(
    backdrop: Backdrop,
    tabs: List<BrowserTab>,
    currentIndex: Int,
    onTabSelect: (Int) -> Unit,
    onTabClose: (Int) -> Unit,
    onNewTab: () -> Unit,
    onClose: () -> Unit
) {
    val settings = LocalBrowserSettings.current
    Box(
        Modifier
            .fillMaxSize()
            .drawBackdrop(
                backdrop = backdrop,
                shape = { androidx.compose.ui.graphics.RectangleShape },
                effects = {
                    vibrancy()
                    blur(8.dp.toPx() * settings.blurIntensity)
                },
                onDrawSurface = {
                    drawRect(Color.Black.copy(alpha = (0.4f * settings.tintIntensity).coerceIn(0f, 1f)))
                }
            )
            .liquidClickable(onClick = onClose)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tabs",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { androidx.compose.ui.graphics.RectangleShape },
                            effects = { vibrancy() }
                        )
                )
                Box(
                    Modifier
                        .size(42.dp)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                blur(4.dp.toPx() * settings.blurIntensity)
                            },
                            onDrawSurface = { drawRect(Color.White.copy(alpha = (0.2f * settings.tintIntensity).coerceIn(0f, 1f))) }
                        )
                        .clip(CircleShape)
                        .liquidClickable(onClick = onNewTab),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, "New Tab", tint = Color.White)
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(tabs) { index, tab ->
                    Box(
                        Modifier
                            .aspectRatio(0.75f)
                            .drawBackdrop(
                                backdrop = backdrop,
                        shape = { RoundedRectangle(24.dp) },
                                effects = {
                                    vibrancy()
                                    blur(2.dp.toPx() * settings.blurIntensity)
                                    lens(
                                        refractionHeight = 10.dp.toPx(),
                                        refractionAmount = 20.dp.toPx()
                                    )
                                },
                                onDrawSurface = {
                                    drawRect(
                                        if (index == currentIndex) Color.White.copy(alpha = (0.4f * settings.tintIntensity).coerceIn(0f, 1f))
                                        else Color.White.copy(alpha = (0.15f * settings.tintIntensity).coerceIn(0f, 1f))
                                    )
                                }
                            )
                            .clip(RoundedCornerShape(24.dp))
                            .border(
                                2.dp,
                                if (index == currentIndex) Color.White.copy(alpha = 0.5f) else Color.Transparent,
                                RoundedCornerShape(24.dp)
                            )
                            .liquidClickable { onTabSelect(index) }
                    ) {
                        tab.snapshot?.let {
                            Image(
                                bitmap = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .graphicsLayer { alpha = 0.6f },
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (tab.isPrivate) {
                                    Icon(
                                        Icons.Default.AddBox, 
                                        null,
                                        Modifier.size(14.dp).padding(end = 4.dp),
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    tab.title,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    Icons.Default.Close,
                                    null,
                                    Modifier
                                        .size(20.dp)
                                        .liquidClickable { onTabClose(index) },
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                tab.url,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryPage(history: List<HistoryEntry>, onNavigate: (String) -> Unit, onClear: () -> Unit, onClose: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .liquidClickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back")
            }
            Text("History", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Clear", color = Color.Red, modifier = Modifier.liquidClickable(onClick = onClear))
        }
        LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp)) {
            items(history) { entry ->
                Column(Modifier.fillMaxWidth().liquidClickable { onNavigate(entry.url) }.padding(vertical = 12.dp)) {
                    Text(entry.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 16.sp)
                    Text(entry.url, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun BookmarksPage(bookmarks: List<Bookmark>, onNavigate: (String) -> Unit, onDelete: (Bookmark) -> Unit, onClose: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .liquidClickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back")
            }
            Text("Bookmarks", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp)) {
            items(bookmarks) { bookmark ->
                Row(Modifier.fillMaxWidth().liquidClickable { onNavigate(bookmark.url) }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bookmark, null, Modifier.size(24.dp), tint = Color(0xFFFFD700))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(bookmark.title, maxLines = 1, fontSize = 16.sp)
                        Text(bookmark.url, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Default.Delete, null, Modifier.size(20.dp).liquidClickable { onDelete(bookmark) }, tint = Color.Gray)
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun SettingsPreview(settings: BrowserSettings) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        val gridBackdrop = rememberLayerBackdrop {
            drawRect(Color(0xFF1A1A2E))
            drawContent()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .layerBackdrop(gridBackdrop)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(24) { index -> 
                val colors = listOf(
                    0xFF0088FF, 0xFF00C853, 0xFFFF6D00, 0xFFD50000,
                    0xFFAA00FF, 0xFF00BCD4, 0xFF76FF03, 0xFFFFAB00
                )
                val color = Color(colors[index % colors.size])
                Box(
                    Modifier
                        .aspectRatio(1f)
                        .background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${index + 1}",
                        color = color.copy(alpha = 0.7f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        val surfaceAlpha = (0.45f * settings.tintIntensity).coerceIn(0f, 1f)

        key(settings.blurIntensity, settings.tintIntensity) {
        Row(
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 4.dp)
                .height(48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = gridBackdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(1.dp.toPx() * settings.blurIntensity)
                            lens(
                                refractionHeight = 12.dp.toPx(),
                                refractionAmount = 24.dp.toPx(),
                                depthEffect = true,
                                chromaticAberration = true
                            )
                        },
                        onDrawSurface = {
                            drawRect(Color.White.copy(alpha = surfaceAlpha))
                        },
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            }

            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = gridBackdrop,
                        shape = { RoundedRectangle(24.dp) },
                        effects = {
                            vibrancy()
                            blur(2.dp.toPx() * settings.blurIntensity)
                            lens(
                                refractionHeight = 15.dp.toPx(),
                                refractionAmount = 30.dp.toPx(),
                                depthEffect = true,
                                chromaticAberration = true
                            )
                        },
                        onDrawSurface = {
                            drawRect(Color.White.copy(alpha = surfaceAlpha))
                        },
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Search",
                        color = Color.Black.copy(alpha = 0.3f),
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }

            Box(
                Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = gridBackdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(1.dp.toPx() * settings.blurIntensity)
                            lens(
                                refractionHeight = 12.dp.toPx(),
                                refractionAmount = 24.dp.toPx(),
                                depthEffect = true,
                                chromaticAberration = true
                            )
                        },
                        onDrawSurface = {
                            drawRect(Color.White.copy(alpha = surfaceAlpha))
                        },
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MoreHoriz,
                    contentDescription = "More",
                    tint = Color.Black.copy(alpha = 0.5f)
                )
            }
        }
        }
    }
}

@Composable
fun SettingsPage(
    backdrop: Backdrop,
    onBlurChange: (Float) -> Unit,
    onTintChange: (Float) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val settings = LocalBrowserSettings.current
    var isDeveloperMode by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
        isDeveloperMode = prefs.getBoolean("developer_mode", false)
    }

    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    Column(
        Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color.Black else Color.White)
            .statusBarsPadding()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .liquidClickable(onClick = onClose),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = textColor)
                }
                Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
            Column(Modifier.padding(16.dp).weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Developer Mode", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
                        Text("Enables JS Console and Edit Page", fontSize = 12.sp, color = textColor.copy(alpha = 0.6f))
                    }
                    LiquidToggle(
                        selected = { isDeveloperMode },
                        onSelect = { 
                            isDeveloperMode = it
                            val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putBoolean("developer_mode", it).apply()
                        },
                        backdrop = backdrop
                    )
                }
                
                HorizontalDivider(color = textColor.copy(alpha = 0.15f))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Blur Intensity", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
                    LiquidSlider(
                        value = { settings.blurIntensity },
                        onValueChange = onBlurChange,
                        valueRange = 0f..5f,
                        visibilityThreshold = 0.001f,
                        backdrop = backdrop
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tinting Intensity", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
                    LiquidSlider(
                        value = { settings.tintIntensity },
                        onValueChange = onTintChange,
                        valueRange = 0f..2f,
                        visibilityThreshold = 0.01f,
                        backdrop = backdrop
                    )
                }

                HorizontalDivider(color = textColor.copy(alpha = 0.15f))
                
                Text("Preview", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
                SettingsPreview(settings)
                
                HorizontalDivider(color = textColor.copy(alpha = 0.15f))
                
                Text("Version ${BuildConfig.VERSION_NAME}", fontSize = 16.sp, color = textColor.copy(alpha = 0.7f))
                Text("Engine: GeckoView", fontSize = 16.sp, color = textColor.copy(alpha = 0.7f))
            }
        }
}

@Composable
fun DevConsolePage(tab: BrowserTab, onExecute: (String) -> Unit, onClose: () -> Unit) {
    var command by remember { mutableStateOf("") }
    
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) 
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .liquidClickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = Color.White)
            }
            Text("JS Console", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.weight(1f))
            Text("Clear", color = Color(0xFF2196F3), modifier = Modifier.liquidClickable { tab.consoleLogs.clear() })
        }
        
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(tab.consoleLogs) { log ->
                Text(
                    text = log,
                    color = if (log.startsWith("Error")) Color.Red else if (log.startsWith("->")) Color(0xFF4CAF50) else Color.White,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF252525))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = command,
                onValueChange = { command = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textStyle = TextStyle(color = Color.White, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { inner ->
                    if (command.isEmpty()) Text("Execute JS...", color = Color.Gray, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    inner()
                }
            )
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3))
                    .clickable { 
                        if (command.isNotBlank()) {
                            tab.consoleLogs.add("> $command")
                            onExecute(command)
                            command = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun DownloadsPage(
    downloads: List<DownloadItem>,
    onAction: (DownloadItem, String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .liquidClickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back")
            }
            Text("Downloads", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        
        if (downloads.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No downloads yet", color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(downloads) { item ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .liquidClickable { 
                                if (item.status == DownloadManager.STATUS_SUCCESSFUL) {
                                    onAction(item, "open")
                                }
                            }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (item.status) {
                                    DownloadManager.STATUS_SUCCESSFUL -> Icons.Default.DownloadDone
                                    DownloadManager.STATUS_RUNNING -> Icons.Default.Downloading
                                    else -> Icons.Default.Download
                                },
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = if (item.status == DownloadManager.STATUS_SUCCESSFUL) Color(0xFF4CAF50) else Color.Gray
                            )
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                Text(item.url, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            
                            Row {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    null,
                                    Modifier.size(20.dp).liquidClickable { onAction(item, "copy") },
                                    tint = Color.Gray
                                )
                                Spacer(Modifier.width(12.dp))
                                Icon(
                                    Icons.Default.Delete,
                                    null,
                                    Modifier.size(20.dp).liquidClickable { onAction(item, "cancel") },
                                    tint = Color.Red.copy(alpha = 0.6f)
                                )
                            }
                        }
                        
                        if (item.status == DownloadManager.STATUS_RUNNING || item.status == DownloadManager.STATUS_PENDING || item.status == DownloadManager.STATUS_PAUSED) {
                            Spacer(Modifier.height(8.dp))
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth(item.progress.coerceIn(0f, 1f))
                                        .fillMaxHeight()
                                        .background(Color(0xFF2196F3), CircleShape)
                                )
                            }
                            Row(
                                Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${(item.progress * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                val sizeStr = if (item.totalSize > 0) {
                                    "${formatSize(item.downloadedSize)} / ${formatSize(item.totalSize)}"
                                } else {
                                    formatSize(item.downloadedSize)
                                }
                                Text(sizeStr, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                    HorizontalDivider(Modifier.padding(top = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun LiquidSlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    visibilityThreshold: Float,
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor =
        if (isLightTheme) Color(0xFF0088FF)
        else Color(0xFF0091FF)
    val trackColor =
        if (isLightTheme) Color(0xFF787878).copy(0.2f)
        else Color(0xFF787880).copy(0.36f)

    val density = LocalDensity.current
    val trackBackdrop = rememberLayerBackdrop()

    BoxWithConstraints(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        val trackWidth = constraints.maxWidth
        val thumbWidthPx = with(density) { 40.dp.toPx() }

        val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
        val animationScope = rememberCoroutineScope()
        var didDrag by remember { mutableStateOf(false) }
        var dragValue by remember { mutableFloatStateOf(value()) }

        LaunchedEffect(value()) {
            if (!didDrag) {
                dragValue = value()
            }
        }

        val dampedDragAnimation = remember(animationScope) {
            DampedDragAnimation(
                animationScope = animationScope,
                initialValue = value(),
                valueRange = valueRange,
                visibilityThreshold = visibilityThreshold,
                initialScale = 1f,
                pressedScale = 1.5f,
                onDragStarted = {
                    didDrag = false
                },
                onDragStopped = {
                    if (didDrag) {
                        onValueChange(dragValue)
                    }
                    didDrag = false
                },
                onDrag = { _, dragAmount ->
                    didDrag = true
                    val range = valueRange.endInclusive - valueRange.start
                    val delta = range * (dragAmount.x / trackWidth)
                    dragValue = (if (isLtr) dragValue + delta else dragValue - delta).coerceIn(valueRange)
                    updateValue(dragValue)
                    onValueChange(dragValue)
                }
            )
        }
        LaunchedEffect(dampedDragAnimation) {
            snapshotFlow { value() }
                .collectLatest { value ->
                    if (dampedDragAnimation.targetValue != value) {
                        dampedDragAnimation.updateValue(value)
                    }
                }
        }

        Box(Modifier.layerBackdrop(trackBackdrop)) {
            Box(
                Modifier
                    .clip(Capsule())
                    .background(trackColor)
                    .pointerInput(animationScope) {
                        detectTapGestures { position ->
                            val delta = (valueRange.endInclusive - valueRange.start) * (position.x / trackWidth)
                            val targetValue =
                                (if (isLtr) valueRange.start + delta
                                else valueRange.endInclusive - delta)
                                    .coerceIn(valueRange)
                            dampedDragAnimation.animateToValue(targetValue)
                            onValueChange(targetValue)
                        }
                    }
                    .height(6f.dp)
                    .fillMaxWidth()
            )

            Box(
                Modifier
                    .clip(Capsule())
                    .background(accentColor)
                    .height(6f.dp)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val width = (constraints.maxWidth * dampedDragAnimation.progress).fastRoundToInt()
                        layout(width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            )
        }

        Box(
            Modifier
                .offset {
                    IntOffset(
                        x = (-thumbWidthPx / 2f + trackWidth * dampedDragAnimation.progress)
                            .fastCoerceIn(-thumbWidthPx / 4f, trackWidth - thumbWidthPx * 3f / 4f)
                            .let { if (isLtr) it else -it }
                            .fastRoundToInt(),
                        y = 0
                    )
                }
                .then(dampedDragAnimation.modifier)
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(backdrop, trackBackdrop),
                    shape = { Capsule() },
                    effects = {
                        val progress = dampedDragAnimation.pressProgress
                        blur(8f.dp.toPx() * (1f - progress))
                        lens(
                            10f.dp.toPx() * progress,
                            14f.dp.toPx() * progress,
                            chromaticAberration = true
                        )
                    },
                    highlight = {
                        val progress = dampedDragAnimation.pressProgress
                        Highlight.Ambient.copy(
                            width = Highlight.Ambient.width / 1.5f,
                            blurRadius = Highlight.Ambient.blurRadius / 1.5f,
                            alpha = progress
                        )
                    },
                    shadow = {
                        Shadow(
                            radius = 4f.dp,
                            color = Color.Black.copy(alpha = 0.05f)
                        )
                    },
                    innerShadow = {
                        val progress = dampedDragAnimation.pressProgress
                        InnerShadow(
                            radius = 4f.dp * progress,
                            alpha = progress
                        )
                    },
                    layerBlock = {
                        scaleX = dampedDragAnimation.scaleX
                        scaleY = dampedDragAnimation.scaleY
                        val velocity = dampedDragAnimation.velocity / 10f
                        scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                        scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                    },
                    onDrawSurface = {
                        val progress = dampedDragAnimation.pressProgress
                        drawRect(Color.White.copy(alpha = 1f - progress))
                    }
                )
                .size(40f.dp, 24f.dp)
        )
    }
}

@Composable
fun LiquidToggle(
    selected: () -> Boolean,
    onSelect: (Boolean) -> Unit,
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor =
        if (isLightTheme) Color(0xFF34C759)
        else Color(0xFF30D158)
    val trackColor =
        if (isLightTheme) Color(0xFF787878).copy(0.2f)
        else Color(0xFF787880).copy(0.36f)

    val density = LocalDensity.current
    val toggleThumbWidthPx = with(density) { 40.dp.toPx() }
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val dragWidth = with(density) { 20f.dp.toPx() }
    val paddingPx = with(density) { 2.dp.toPx() }
    val animationScope = rememberCoroutineScope()
    var didDrag by remember { mutableStateOf(false) }
    var fraction by remember { mutableFloatStateOf(if (selected()) 1f else 0f) }
    val dampedDragAnimation = remember(animationScope) {
        DampedDragAnimation(
            animationScope = animationScope,
            initialValue = fraction,
            valueRange = 0f..1f,
            visibilityThreshold = 0.001f,
            initialScale = 1f,
            pressedScale = 1.5f,
            onDragStarted = {},
            onDragStopped = {
                if (didDrag) {
                    fraction = if (targetValue >= 0.5f) 1f else 0f
                    onSelect(fraction == 1f)
                    didDrag = false
                } else {
                    fraction = if (selected()) 0f else 1f
                    onSelect(fraction == 1f)
                }
            },
            onDrag = { _, dragAmount ->
                if (!didDrag) {
                    didDrag = dragAmount.x != 0f
                }
                val delta = dragAmount.x / dragWidth
                fraction =
                    if (isLtr) (fraction + delta).fastCoerceIn(0f, 1f)
                    else (fraction - delta).fastCoerceIn(0f, 1f)
                updateValue(if (isLtr) fraction else 1f - fraction)
            }
        )
    }
    LaunchedEffect(dampedDragAnimation) {
        snapshotFlow { fraction }
            .collectLatest { fraction ->
                dampedDragAnimation.updateValue(fraction)
            }
    }
    LaunchedEffect(selected) {
        snapshotFlow { selected() }
            .collectLatest { isSelected ->
                val target = if (isSelected) 1f else 0f
                if (target != fraction) {
                    fraction = target
                    dampedDragAnimation.animateToValue(target)
                }
            }
    }

    val trackBackdrop = rememberLayerBackdrop()

    Box(
        modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .layerBackdrop(trackBackdrop)
                .clip(Capsule())
                .drawBehind {
                    val fraction = dampedDragAnimation.value
                    drawRect(lerp(trackColor, accentColor, fraction))
                }
                .size(64f.dp, 28f.dp)
        )

        Box(
            Modifier
                .offset {
                    val fraction = dampedDragAnimation.value
                    IntOffset(
                        x = (if (isLtr) lerp(paddingPx, paddingPx + dragWidth, fraction)
                              else lerp(-paddingPx, -(paddingPx + dragWidth), fraction))
                            .fastRoundToInt(),
                        y = 0
                    )
                }
                .semantics {
                    role = Role.Switch
                }
                .then(dampedDragAnimation.modifier)
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(backdrop, trackBackdrop),
                    shape = { Capsule() },
                    effects = {
                        val progress = dampedDragAnimation.pressProgress
                        blur(8f.dp.toPx() * (1f - progress))
                        lens(
                            5f.dp.toPx() * progress,
                            10f.dp.toPx() * progress,
                            chromaticAberration = true
                        )
                    },
                    highlight = {
                        val progress = dampedDragAnimation.pressProgress
                        Highlight.Ambient.copy(
                            width = Highlight.Ambient.width / 1.5f,
                            blurRadius = Highlight.Ambient.blurRadius / 1.5f,
                            alpha = progress
                        )
                    },
                    shadow = {
                        Shadow(
                            radius = 4f.dp,
                            color = Color.Black.copy(alpha = 0.05f)
                        )
                    },
                    innerShadow = {
                        val progress = dampedDragAnimation.pressProgress
                        InnerShadow(
                            radius = 4f.dp * progress,
                            alpha = progress
                        )
                    },
                    layerBlock = {
                        scaleX = dampedDragAnimation.scaleX
                        scaleY = dampedDragAnimation.scaleY
                        val velocity = dampedDragAnimation.velocity / 50f
                        scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                        scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                    },
                    onDrawSurface = {
                        val progress = dampedDragAnimation.pressProgress
                        drawRect(Color.White.copy(alpha = 1f - progress))
                    }
                )
                .size(40f.dp, 24f.dp)
        )
    }
}

private fun formatSize(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format("%.1f %s", bytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

private fun resolveUrl(query: String): String {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return "https://www.google.com"

    val urlPattern = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/.*)?$".toRegex()
    return if (trimmed.matches(urlPattern)) {
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else {
            trimmed
        }
    } else {
        try {
            "https://www.google.com/search?q=" + java.net.URLEncoder.encode(trimmed, "UTF-8")
        } catch (_: Exception) {
            "https://www.google.com/search?q=$trimmed"
        }
    }
}

@Composable
fun Modifier.liquidClickable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    val scope = rememberCoroutineScope()
    val dampedDrag = remember {
        DampedDragAnimation(
            animationScope = scope,
            initialValue = 0f,
            valueRange = 0f..0f,
            visibilityThreshold = 0.01f,
            initialScale = 1f,
            pressedScale = 0.95f,
            onDragStarted = {},
            onDragStopped = {},
            onDrag = { _, _ -> }
        )
    }
    return this
        .graphicsLayer {
            scaleX = dampedDrag.scaleX
            scaleY = dampedDrag.scaleY
        }
        .then(dampedDrag.modifier)
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(),
            enabled = enabled,
            onClick = onClick
        )
}

private fun saveHistory(context: Context, history: List<HistoryEntry>) {
    val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
    val array = org.json.JSONArray()
    history.forEach { entry ->
        val obj = org.json.JSONObject()
        obj.put("title", entry.title)
        obj.put("url", entry.url)
        obj.put("timestamp", entry.timestamp)
        array.put(obj)
    }
    prefs.edit().putString("history", array.toString()).apply()
}

private fun loadHistory(context: Context): List<HistoryEntry> {
    val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
    val historyStr = prefs.getString("history", null) ?: return emptyList()
    val list = mutableListOf<HistoryEntry>()
    try {
        val array = org.json.JSONArray(historyStr)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(HistoryEntry(
                obj.getString("title"),
                obj.getString("url"),
                obj.optLong("timestamp", System.currentTimeMillis())
            ))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list
}

private fun saveBookmarks(context: Context, bookmarks: List<Bookmark>) {
    val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
    val array = org.json.JSONArray()
    bookmarks.forEach { bookmark ->
        val obj = org.json.JSONObject()
        obj.put("title", bookmark.title)
        obj.put("url", bookmark.url)
        array.put(obj)
    }
    prefs.edit().putString("bookmarks", array.toString()).apply()
}

private fun loadBookmarks(context: Context): List<Bookmark> {
    val prefs = context.getSharedPreferences("browser_prefs", Context.MODE_PRIVATE)
    val bookmarksStr = prefs.getString("bookmarks", null) ?: return emptyList()
    val list = mutableListOf<Bookmark>()
    try {
        val array = org.json.JSONArray(bookmarksStr)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(Bookmark(
                obj.getString("title"),
                obj.getString("url")
            ))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list
}

private fun parseCssColor(colorStr: String): Color? {
    return try {
        if (colorStr.startsWith("rgb")) {
            val vals = colorStr.substringAfter("(").substringBefore(")").split(",").map { it.trim().toInt() }
            if (vals.size >= 3) {
                Color(vals[0], vals[1], vals[2], vals.getOrNull(3) ?: 255)
            } else null
        } else if (colorStr.startsWith("#")) {
            Color(colorStr.toColorInt())
        } else null
    } catch (_: Exception) {
        null
    }
}

@Composable
fun MenuHeaderItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .liquidClickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            label,
            color = Color.Black.copy(alpha = 0.7f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun MenuListItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .liquidClickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            label,
            color = Color.Black.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
} }
