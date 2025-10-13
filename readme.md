
XLinearCalendar: Linear and Grid Calendar Composables for Jetpack Compose
=========================================================================
[![JitPack](https://img.shields.io/jitpack/v/github/TuleSimon/XCalendar.svg?color=blue)](https://jitpack.io/#TuleSimon/XCalendar)

<p> 
  <img src="./screenshots/img.webp" alt="cover">
</p>

## 💡 Description

**XLinearCalendar** provides a set of highly flexible and performant calendar components for Jetpack Compose, offering both an infinite horizontal linear calendar view and a customizable monthly grid view. Both components allow for complete UI customization via composable lambdas.

✨ Features
----------

*   **Two Core Components:** XLinearCalendar (Horizontal Scroll) and XLinearGridCalendar (Monthly Grid).

*   **Infinite Loading:** Easily enable automatic loading of next/previous dates or months.

*   **Bounded Calendar:** Limit the visible date range for specific use cases (e.g., booking).

*   **Full UI Customization:** The appearance of individual dates, months, and days is fully controlled by your own composable functions (content, monthContent, dayContent).

*   **State Management:** Utilize rememberXLinearCalendarState or XCalendarController for programmatic control over selection and scrolling.


📦 Installation
---------------

This library is currently published to GitHub Packages. Follow these steps to include it in your Android project.

### 1\. Configure GitHub Packages Repository

Add the GitHub Packages URL to your **project-level** settings.gradle.kts file:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   dependencyResolutionManagement {      repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)      repositories {          google()          mavenCentral()          // Add GitHub Packages repository          maven {              name = "GitHubPackages"              url = uri("[https://maven.pkg.github.com/tulesimon/XCalendar](https://maven.pkg.github.com/tulesimon/XCalendar)")              credentials {                  // Ensure your GitHub token (gpr.token) and user (gpr.user) are set in your                  // ~/.gradle/gradle.properties or local.properties                  username = project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_ACTOR")                  password = project.findProperty("gpr.token") as? String ?: System.getenv("GITHUB_TOKEN")              }          }      }  }   `

### 2\. Add Dependency

In your **module-level** build.gradle.kts file:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   dependencies {      // Core calendar component      implementation("com.anonymous:xlinearcalendar:1.0.0")      // If you need kotlinx-datetime support (recommended for modern Kotlin date handling)      implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")  }   `

🚀 Usage Examples
-----------------

### 1\. XLinearCalendar (Horizontal Date Scroll)

This component is ideal for displaying a compact, continuously scrolling list of days or for a date picker.

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   // Example from MainActivity.kt  val customDates = listOf( /* ... list of Date objects ... */ )  XLinearCalendar(      // Optional: limit the height of the component      modifier = Modifier.height(100.dp),      // Optional: pre-select initial dates      initialDates = customDates,      // Customize the appearance of each date cell      content = { date, isSelected, onClick ->          DateCell(              date = date,              isSelected = isSelected,              onDateSelected = onClick,              modifier = Modifier.padding(6.dp)          )      }  )   `

**Bounded Calendar Example**

You can limit the date range (e.g., for a booking system):

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   @Composable  fun BoundedCalendarExample() {      XLinearCalendar(          isBounded = true,          maxDays = 90, // Only display the next 90 days from the start date          shouldLoadNext = false,          shouldLoadPrevious = false,          state = rememberXLinearCalendarState(instanceName = "BookingInstance"),          content = { date, isSelected, onClick ->              // Your custom DateCell UI              DateCell(date = date, isSelected = isSelected, onDateSelected = onClick)          }      )  }   `

### 2\. XLinearGridCalendar (Monthly Grid View)

This component displays a full monthly grid and allows horizontal scrolling between months, fully customizable through two content lambdas.

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   XLinearGridCalendar(      // By default, loads months infinitely in both directions      shouldLoadNext = true,      shouldLoadPrevious = true,      // Custom Composable for the Month Header (e.g., "October 2025")      monthContent = { monthDate, isSelected, onClick ->          DefaultGridMonthCell(              month = monthDate,              isSelected = isSelected,              onMonthSelected = onClick,              modifier = Modifier.padding(6.dp)          )      },      // Custom Composable for each Day Cell      dayContent = { dayDate, isSelected, onClick ->          DefaultGridDayCell(              date = dayDate,              isSelected = isSelected,              onDateSelected = onClick,              modifier = Modifier.padding(6.dp)          )      },  )   `

🛠️ API & Customization
-----------------------

The power of this library comes from the composable lambda arguments, which allow you to inject your own UI for different parts of the calendar.

Component

Lambda Parameter

Description

XLinearCalendar

content: @Composable (Date, Boolean, (Date) -> Unit) -> Unit

Renders the individual date cell in the horizontal list.

XLinearGridCalendar

monthContent: @Composable (Date, Boolean, (Date) -> Unit) -> Unit

Renders the header (the month title) in the grid view.

XLinearGridCalendar

dayContent: @Composable (Date?, Boolean, (Date) -> Unit) -> Unit

Renders each day cell in the monthly grid. Date? is null for filler days.

**State**

state: XLinearCalendarState

Required for programmatic control and state persistence (see rememberXLinearCalendarState).

🤝 Contributing
---------------

Contributions are welcome! If you find a bug or have a feature request, please open an issue on the [GitHub Repository](https://github.com/TuleSimon/XCalendar).

📄 License
----------

This library is licensed under the Apache License, Version 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.txt) file for details.