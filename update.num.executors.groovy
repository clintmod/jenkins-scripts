import groovy.xml.XmlUtil 

filePath = '/opt/sites/.jenkins/config.xml'
fileContents = new File(filePath).text

def config = new XmlSlurper().parseText(fileContents)

config.slaves[0].slave.each {
  it.numExecutors = 5
}

def writer = new FileWriter(filePath)
newFileContents = XmlUtil.serialize(config, writer)
