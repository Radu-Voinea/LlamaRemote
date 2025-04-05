rootProject.name = "llama_remote"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

fun createProject(module: String, path: String) {
    include(module)
    project(module).projectDir = file(path)
}

fun createProject(module: String) {
    val modulePath: String = module
        .replace(":", "")
        .replace(".", "/")

    val fullPath = "src/$modulePath"
    val moduleDir = File(fullPath)

    if (!moduleDir.exists() || ! moduleDir.isDirectory) {
        println("[!] Could not create module $module. Path $fullPath does not exist or is not a directory.")
    }

    createProject(module, fullPath)
}

createProject(":extension")
createProject(":common")
createProject(":server")