#!/usr/bin/env groovy

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/LabVIEW-DCAF/buildsystem

def call(lvVersion, diffingPicRepo) {
    node(lvVersion) {
        echo 'Starting build...'
        stage('Pre-Clean') {
            preClean()
        }
        stage('SCM_Checkout') {
            echo 'Attempting to get source from repo...'
            timeout(time: 5, unit: 'MINUTES') {
                checkout scm
            }

            timeout(time: 5, unit: 'MINUTES') {
                cloneDcafBuildTools()
            }
        }
        // If this change is a pull request, diff vis.
        if (env.CHANGE_ID) {
            stage('Diff VIs') {
                timeout(time: 60, unit: 'MINUTES') {
                    lvDiff(lvVersion, diffingPicRepo)
                    echo 'Diff Succeeded!'
                }
            }
        }
        stage('Post-Clean') {
            postClean()
        }
    }
}
