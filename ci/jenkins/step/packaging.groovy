timeout(time: 5, unit: 'MINUTES') {
    dir ("ci/jenkins/scripts") {
        sh "./packaging.sh"
        withCredentials([usernamePassword(credentialsId: "${params.JFROG_CREDENTIALS_ID}", usernameVariable: 'JFROG_USERNAME', passwordVariable: 'JFROG_PASSWORD')]) {
            def uploadStatus = sh(returnStatus: true, script: "curl -u${JFROG_USERNAME}:${JFROG_PASSWORD} -T ./${PROJECT_NAME}-${PACKAGE_VERSION}.tar.gz ${params.JFROG_ARTFACTORY_URL}/milvus/package/${PROJECT_NAME}-${PACKAGE_VERSION}.tar.gz")
            if (uploadStatus != 0) {
                error("\" ${PROJECT_NAME}-${PACKAGE_VERSION}.tar.gz \" upload to \" ${params.JFROG_ARTFACTORY_URL}/milvus/package/${PROJECT_NAME}-${PACKAGE_VERSION}.tar.gz \" failed!")
            }
        }
    }
}
