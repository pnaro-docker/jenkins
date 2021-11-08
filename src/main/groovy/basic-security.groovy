#!groovy
import hudson.security.*
import jenkins.install.InstallState
import jenkins.model.*
import hudson.model.*;
//plugins
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import org.jenkinsci.plugins.workflow.job.WorkflowJob

def instance = Jenkins.getInstanceOrNull()

if (instance.installState.isSetupComplete()) {
    println "--> jenkins is initialized"
    def initJob = System.getenv("JENKINS_INIT_JOB")
    if(initJob == null){
        return
    }
    def job = instance.getItemByFullName(initJob)
    if(job != null && job instanceof WorkflowJob){
        def workflowJob = (WorkflowJob) job
        if(workflowJob.getLastBuild() == null){
            def adminUser = System.getenv("JENKINS_ADMIN")
            workflowJob.scheduleBuild(new Cause.UserIdCause(adminUser))
        }
    }
    return
}

def adminUser = System.getenv("JENKINS_ADMIN")
def adminPassword = System.getenv("JENKINS_PASSWORD")

println "--> creating local user"
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", UUID.randomUUID().toString())
hudsonRealm.createAccount(adminUser, adminPassword)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

jlc = JenkinsLocationConfiguration.get()
jlc.setUrl(System.getenv("JENKINS_URL"))

println '--> Neutering SetupWizard'
InstallState.INITIAL_SETUP_COMPLETED.initializeState()

// disable Job DSL script approval
def configuration = GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class)
configuration.setUseScriptSecurity(false)
configuration.save()

instance.save()

println '--> Restarting after initialize'
instance.doSafeRestart(null)