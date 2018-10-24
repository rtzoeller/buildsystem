def call() {
    echo 'Cloning build tools to workspace.'

    def organization = getComponentParts()['organization']
    def branch = env."library.dcaf-build-tools.version"
    buildToolsDir = cloneRepo("https://github.com/rtzoeller/buildsystem", branch)
    return buildToolsDir
}
