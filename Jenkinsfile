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
    }

    post {
        always {
            echo 'Cleaning up...'
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
    def status = currentBuild.result ?: 'SUCCESS'
    def color = status == 'SUCCESS' ? 'green' : 'red'
    def message = status == 'SUCCESS' ? '构建成功' : '构建失败'

    sh """
        curl -X POST 'https://open.feishu.cn/open-apis/bot/v2/hook/a269846c-3b2b-4da8-9318-6769bdfd44da' \
        -H 'Content-Type: application/json' \
        -d '{
            "msg_type": "interactive",
            "card": {
                "header": {
                    "title": {"tag": "plain_text", "content": "Jenkins 构建通知"},
                    "template": "'${color}'"
                },
                "elements": [
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**项目名称:** jenkins-demo"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建状态:** '${message}'"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建编号:** #'${BUILD_NUMBER}'"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**触发用户:** ${BUILD_USER:-'System'}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建耗时:** ${currentBuild.durationString}"}},
                    {"tag": "div", "text": {"tag": "lark_md", "content": "**构建日志:** [点击查看](${env.BUILD_URL})"}}
                ]
            }
        }'
    """
}
