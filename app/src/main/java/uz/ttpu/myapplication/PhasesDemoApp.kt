package uz.ttpu.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhasesDemoApp() {
    var currentDemo by remember { mutableStateOf<String>("Menu") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (currentDemo == "Menu") "Practice 7: Jetpack Compose Phases" else "Demo: $currentDemo") },
                    actions = {
                        if (currentDemo != "Menu") {
                            Button(onClick = { currentDemo = "Menu" }) {
                                Text("Back to Menu")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues).fillMaxSize()) {
                when (currentDemo) {
                    "Menu" -> {
                        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                            item { DemoButton("Q4: Lecture Tree", "LectureTree") { currentDemo = "LectureTree" } }
                            item { DemoButton("Q5: Padding Demo", "PaddingDemo") { currentDemo = "PaddingDemo" } }
                            item { DemoButton("Q6: Offset Demo", "OffsetDemo") { currentDemo = "OffsetDemo" } }
                            item { DemoButton("Q6: Offset Demo Exp", "OffsetDemoExp") { currentDemo = "OffsetDemoExp" } }
                            item { DemoButton("Q7: Color Demo (Canvas)", "ColorDemo") { currentDemo = "ColorDemo" } }
                            item { DemoButton("Q7: Color Demo (Box)", "ColorDemoBox") { currentDemo = "ColorDemoBox" } }
                            item { DemoButton("Q9: Parallax Slow", "ParallaxSlow") { currentDemo = "ParallaxSlow" } }
                            item { DemoButton("Q9: Parallax Fast", "ParallaxFast") { currentDemo = "ParallaxFast" } }
                            item { DemoButton("Q10: ScrollToTop Naive", "ScrollToTopNaive") { currentDemo = "ScrollToTopNaive" } }
                            item { DemoButton("Q10: ScrollToTop Derived", "ScrollToTopDerived") { currentDemo = "ScrollToTopDerived" } }
                            item { DemoButton("Q11: Skip Parent/Child", "Parent") { currentDemo = "Parent" } }
                            item { DemoButton("Q11: Skip Parent/Child (Lambda)", "ParentLambda") { currentDemo = "ParentLambda" } }
                            item { DemoButton("Q13: Cyclic Demo", "CyclicDemo") { currentDemo = "CyclicDemo" } }
                            item { DemoButton("Q13: Cyclic Fixed", "CyclicFixed") { currentDemo = "CyclicFixed" } }
                            item { DemoButton("Q14: StackBelow Custom Layout", "StackBelow") { currentDemo = "StackBelow" } }
                        }
                    }
                    "LectureTree" -> LectureTree()
                    "PaddingDemo" -> PaddingDemo()
                    "OffsetDemo" -> OffsetDemo()
                    "OffsetDemoExp" -> OffsetDemoExperimental()
                    "ColorDemo" -> ColorDemo()
                    "ColorDemoBox" -> ColorDemoBox()
                    "ParallaxSlow" -> ParallaxSlow()
                    "ParallaxFast" -> ParallaxFast()
                    "ScrollToTopNaive" -> ScrollToTopNaive()
                    "ScrollToTopDerived" -> ScrollToTopDerived()
                    "Parent" -> Parent()
                    "ParentLambda" -> ParentLambda()
                    "CyclicDemo" -> CyclicDemo()
                    "CyclicFixed" -> CyclicFixed()
                    "StackBelow" -> {
                        StackBelow(Modifier.fillMaxWidth(), spacing = 8.dp) {
                            Box(Modifier.fillMaxWidth().height(120.dp).background(Color(0xFF6650A4)))
                            Text("I'm below the image")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DemoButton(title: String, tag: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(title)
    }
}
