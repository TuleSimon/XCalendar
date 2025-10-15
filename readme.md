# XCalendar [![JitPack](https://img.shields.io/jitpack/v/github/TuleSimon/XCalendar.svg?color=blue)](https://jitpack.io/#TuleSimon/XCalendar)
<p> 
  <img  width="60%" height="40%" src="./screenshots/cover.webp" alt="cover">
</p>

## 💡 Description

XCalendar 🗓️
XCalendar is a simple and customizable calendar library built entirely for Jetpack Compose. It provides straightforward, user-friendly, 
and horizontally scrollable calendar components perfect for any modern Android application.

We have two components for different usecases, first one is 

### ✨ Features
Built for Compose: A modern, declarative UI library that integrates seamlessly into your Compose projects.

Two Unique Views: Offers both a compact linear date picker and a traditional grid-style calendar.

Easy Integration: Add powerful calendar functionality to your app with just a few lines of code.

Intuitive Navigation: Smooth horizontal scrolling for navigating through dates and months.

User-Friendly: Designed to provide a clean and intuitive experience for the end-user.


## 🚀 Components

XCalendar offers two primary composables to fit different use cases.

### 1. `XLinearCalendar()`

This component displays a clean, horizontally scrollable **linear row of dates**. It's perfect for implementing compact date pickers, timelines, or any UI that requires a simple, focused view of consecutive days.

**Use Case**: Ideal for booking apps, habit trackers, or anywhere you need a quick date selection without taking up too much screen space.

```kotlin
@Composable
fun MyScreen() {
    XLinearCalendar(
        // Add your configuration and state handling here
    )
}
```
### 2. `XLinearGridCalendar()`
This component displays a horizontally scrollable list of months. When a month is selected, it presents the dates in a classic grid format. This approach combines easy navigation between months with the familiar structure of a traditional calendar.

Use Case: A great choice for event apps, schedulers, or any application that benefits from a full month-at-a-glance view.

```kotlin
@Composable
fun MyScheduler() {
    XLinearGridCalendar(
        // Add your configuration and state handling here
    )
}
```

## 📸 Screenshots
<p>
  <img src="./screenshots/img.webp" width="32%" alt="screenshot_1">

</p>



## ⚙️ Setup
To integrate XCalendar into your project, you'll need to add the appropriate repository configuration to your `settings.gradle(.kts)` file and then include the dependency in your module-level `build.gradle(.kts)` file.

## 1. Repository Configuration (settings.gradle or settings.gradle.kts)

### Option A: Using JitPack
JitPack allows you to use any public GitHub repository as a Maven dependency.

Kotlin DSL (settings.gradle.kts)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("[https://jitpack.io](https://jitpack.io)") }
    }
}
```

### Groovy DSL (settings.gradle)

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url '[https://jitpack.io](https://jitpack.io)' }
    }
}
```

### Option B: Using GitHub Packages (Recommended)

GitHub Packages often requires authentication for reliable access, especially due to rate limiting. You'll need a GitHub Personal Access Token (PAT) with the **`read:packages`** scope, set as an environment variable (`GITHUB_TOKEN`) or a project property (`gpr.token`).

#### Kotlin DSL (`settings.gradle.kts`)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add GitHub Packages repository
        maven {
            name = "GitHubPackages"
            url = uri("[https://maven.pkg.github.com/tulesimon/XCalendar](https://maven.pkg.github.com/tulesimon/XCalendar)")
            credentials {
                // Ensure your GitHub token (gpr.token) and user (gpr.user) are set
                username = project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as? String ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```
#### Groovy DSL (settings.gradle)

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add GitHub Packages repository
        maven {
            name = "GitHubPackages"
            url = uri("[https://maven.pkg.github.com/tulesimon/XCalendar](https://maven.pkg.github.com/tulesimon/XCalendar)")
            credentials {
                // Ensure your GitHub token (gpr.token) and user (gpr.user) are set
                username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### 2. Add the Dependency (build.gradle or build.gradle.kts)
   Once the repository is configured, add the dependency to your module's build.gradle file (e.g., app/build.gradle).

Note: The exact Group ID and Artifact ID will depend on the publishing method (GitHub Packages vs. JitPack). Please replace XCALENDAR_VERSION with the latest release version.

Kotlin DSL (build.gradle.kts)

In your module-level build.gradle.kts:
```kotlin
dependencies {
    // If using Github Packages
    // Check the exact Group ID/Artifact ID published to GitHub Packages
    implementation("com.github.tulesimon:xcalendar:XCALENDAR_VERSION")

    // If using JitPack
    implementation("com.github.tulesimon:XCalendar:XCALENDAR_VERSION")
}
```



**Note**: XCalendar utilizes the `kotlinx-datetime` library, you don't have to include this, as we only expose the java `Date`.



## 📱 Usage
### Basic usage
XCalendar provides a basic implementation of a date cell that can be used to display a simple row calendar.   
The following code snippet shows how to use the different available options

### 1. XLinearCalendar (Horizontal Date Scroll)

This component is ideal for displaying a compact, continuously scrolling list of days or for a date picker.
```kotlin
val customDates = listOf( /* ... list of Date objects ... */ )

XLinearCalendar(
    modifier = Modifier.height(100.dp), // Optional: limit the height
    initialDates = customDates,         // Optional: pre-select initial dates
    content = { date, isSelected, onClick ->
        DateCell(
            date = date,
            isSelected = isSelected,
            onDateSelected = onClick,
            modifier = Modifier.padding(6.dp)
        )
    }
)

```   

Bounded Calendar Example

You can limit the date range (e.g., for a booking system):
```kotlin
@Composable
fun BoundedCalendarExample() {
    XLinearCalendar(
        isBounded = true,
        maxDays = 90, // Only display the next 90 days from the start date
        shouldLoadNext = false,
        shouldLoadPrevious = false,
        state = rememberXLinearCalendarState(instanceName = "BookingInstance"),
        content = { date, isSelected, onClick ->
            DateCell(
                date = date,
                isSelected = isSelected,
                onDateSelected = onClick
            )
        }
    )
}

```


### 2. XLinearGridCalendar (Monthly Grid View)

This component displays a full monthly grid and allows horizontal scrolling between months, fully customizable through two content lambdas.

```kotlin
XLinearGridCalendar(
    shouldLoadNext = true,
    shouldLoadPrevious = true,
    monthContent = { monthDate, isSelected, onClick ->
        DefaultGridMonthCell(
            month = monthDate,
            isSelected = isSelected,
            onMonthSelected = onClick,
            modifier = Modifier.padding(6.dp)
        )
    },
    dayContent = { dayDate, isSelected, onClick ->
        DefaultGridDayCell(
            date = dayDate,
            isSelected = isSelected,
            onDateSelected = onClick,
            modifier = Modifier.padding(6.dp)
        )
    }
)

```


## 🛠️ API & Customization

The power of this library comes from the composable lambda arguments, which allow you to inject your own UI for different parts of the calendar.

| Component | Lambda Parameter | Description |
|:---|:---|:---|
| XLinearCalendar | `content` | Renders the individual date cell in the horizontal list. |
| XLinearGridCalendar | `monthContent` | Renders the header (the month title) in the grid view. |
| XLinearGridCalendar | `dayContent` | Renders each day cell in the monthly grid. Date? is null for filler days. |

---

# Full Example 
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XLinearCalendarTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) { innerPadding ->
                    Sample()
                }
            }
        }
    }
}

@Composable
fun Sample() {
    val customDates = listOf(
        Date(2025 - 1900, 9, 10), // Oct 10, 2025
        Date(2025 - 1900, 9, 11),
        Date(2025 - 1900, 9, 12)
    )
    Column(Modifier.padding(horizontal = 16.dp)) {
        XLinearCalendar(
            modifier = Modifier.height(100.dp),
            initialDates = customDates,
            shouldLoadNext = false,
            shouldLoadPrevious = false,
            content = { date, isSelected, onClick ->
                DateCell(
                    date = date,
                    colors = DateCellDefaults.colors().copy(
                        selectedContainerColor = selected,
                        selectedTextColor = Color.White
                    ),
                    isSelected = isSelected,
                    onDateSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )

            }
        )
        BoundedCalendarExample()
        XLinearGridCalendar(
            modifier = Modifier,
            shouldLoadNext = true,
            shouldLoadPrevious = true,
            monthContent = { date, isSelected, onClick ->
                DefaultGridMonthCell(
                    month = date,
                    isSelected = isSelected,
                    onMonthSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )
            },
            dayContent = { date, isSelected, onClick ->
                DefaultGridDayCell(
                    date = date,
                    isSelected = isSelected,
                    colors = DateCellDefaults.colors().copy(
                        selectedContainerColor = selected,
                        selectedTextColor = Color.White
                    ),
                    onDateSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )
            },
        )
    }
}

/** 4. Bounded Calendar with Limits */
@Composable
fun BoundedCalendarExample() {
    XLinearCalendar(
        isBounded = true,
        maxDays = 90,
        shouldLoadNext = false,
        shouldLoadPrevious = false,
        state = rememberXLinearCalendarState(instanceName = "NewInstance"),
        content = { date, isSelected, onClick ->
            DateCell(
                date = date,
                colors = DateCellDefaults.colors().copy(
                    selectedContainerColor = selected,
                    selectedTextColor = Color.White
                ),
                isSelected = isSelected,
                onDateSelected = onClick,
                modifier = Modifier.padding(4.dp)
            )
        }
    )
}
```

## State Information

```markdown
| Component | Lambda Parameter | Description |
|:---|:---|:---|
| XLinearCalendar | `content` | Renders the individual date cell in the horizontal list. |
| XLinearGridCalendar | `monthContent` | Renders the header (the month title) in the grid view. |
| XLinearGridCalendar | `dayContent` | Renders each day cell in the monthly grid. Date? is null for filler days. |

---

## State Information

| Key | Value | Description |
|:---|:---|:---|
| **State Name** | `state: XLinearCalendarState` | Required for programmatic control and state persistence (see `rememberXLinearCalendarState`). |
```

## 🤝 Contribution

If you wish to contribute, please feel free to submit pull requests or issues to help improve XCalendar.

## 💙 Find this repository useful?
If you find this library useful, please consider starring the repository and sharing it with others :star:

# License
```xml
Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
