podTemplate(yaml: '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/jnlp-agent:latest-windows
  - name: shell
    image: mcr.microsoft.com/powershell:preview-windowsservercore-1809
    command:
    - powershell
    args:
    - Start-Sleep
    - 999999
  nodeSelector:
    beta.kubernetes.io/os: windows
'''
) {
    node(POD_LABEL) {
        bat 'mkdir subdir'
        withEnv(["STUFF=some value"]) {
            dir('subdir') {
                container('shell') {
                    bat 'dir'
                    powershell 'Get-ChildItem Env: | Sort Name'
                    bat 'echo got stuff: %STUFF%'
                }
            }
        }
    }
}
