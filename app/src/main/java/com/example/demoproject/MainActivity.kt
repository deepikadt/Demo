package com.example.demoproject

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demoproject.ui.theme.DemoProjectTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SampleWebView()
                }
            }
        }
    }
}

@Composable
fun SampleWebView() {
    val context = LocalContext.current
    val html = HtmlLoader.loadHtml(context = context)
    val encodeHtml = Base64.encodeToString(html.encodeToByteArray(), Base64.NO_PADDING)
    val webView = WebView(context)

    Column(
        modifier = Modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Button(onClick = {
                webView.evaluateJavascript(
                    "javascript:" + "updateFromNative(\"" + "Hi! Message from Native" + "\")",
                    null
                )

        }) {
            Text(text = "Send message: Mobile to Web View")
        }
        AndroidView(
            modifier = Modifier.padding(top = 8.dp),
            update = {
                it.loadData(
                    encodeHtml,
                    "text/html",
                    "base64"
                )
            },
            factory = {
                webView.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
              /* Using setJavaScriptEnabled can introduce XSS vulnerabilities into your application, review carefully */
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(AppWebInterface(context), "Android")
                    loadData(
                        encodeHtml,
                        "text/html",
                        "base64"
                    )
                }
            }
        )
    }
}

internal class HtmlLoader {
    companion object {
        fun loadHtml(context: Context): String {
            val asset = context.assets
            val reader = BufferedReader(
                InputStreamReader(asset.open("demoHtmlFile.html"))
            )
            val newLine = System.getProperty("line.separator")
            return reader.lines().collect(Collectors.joining(newLine))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SampleWebViewPreview() {
    DemoProjectTheme {
        SampleWebView()
    }
}