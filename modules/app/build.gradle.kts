plugins {
    id("dev.iurysouza.livematch.android-application")
}

android{
    buildTypes {
        val USE_MOCK_URL: String by project
        val API_URL: String by project
        val MOCK_API_URL: String by project

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            buildConfigField("String", "API_URL", API_URL)
        }
        getByName("debug") {
            buildConfigField(
                type = "String",
                name = "API_URL",
                value = if (USE_MOCK_URL.toBoolean()) MOCK_API_URL else API_URL)
        }
    }

}
