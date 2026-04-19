pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Run') {
            steps {
                echo 'Running the application...'
                sh 'nohup java -jar target/jenkins-demo-1.0.0.jar > app.log 2>&1 &'
                echo 'Waiting for application to start...'
                sleep 30
                sh 'curl -s http://localhost:9090/hello || (echo "App startup failed, checking logs:" && cat app.log && exit 1)'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            sh 'pkill -f jenkins-demo-1.0.0.jar || true'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
            feishuNotification()
        }
        failure {
            echo 'Pipeline failed!'
            feishuNotification()
        }
    }
}

def feishuNotification() {
    def buildStatus = currentBuild.result ?: 'SUCCESS'
    def color = (buildStatus == 'SUCCESS') ? 'green' : 'red'
    def message = (buildStatus == 'SUCCESS') ? '构建成功' : '构建失败'
    def buildUser = getBuildUser()
    def buildNumber = BUILD_NUMBER ?: 'unknown'
    def buildUrl = env.BUILD_URL ?: 'unknown'
    def durationStr = currentBuild.durationString ?: 'unknown'

    sh """
        curl -X POST 'https://open.feishu.cn/open-apis/bot/v2/hook/a269846c-3b2b-4da8-9318-6769bdfd44da' \
        -H 'Content-Type: application/json' \
        -d '{
            "msg_type": "interactive",
            "card": {
                "header": {
                    "title": {"tag": "plain_text", "content": "Jenkins 构建通知"},
                    "template": "${color}"
                },
                "elements": [
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**项目名称:** jenkins-demo"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建状态:** ${message}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建编号:** #${buildNumber}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**触发用户:** ${buildUser}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建耗时:** ${durationStr}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建日志:** [点击查看](${buildUrl})"}}
                ]
            }
        }'
    """
}

def getBuildUser() {
    try {
        def cause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)
        return cause ? cause.getUserId() : 'System'
    } catch (Exception e) {
        return 'System'
    }
}
