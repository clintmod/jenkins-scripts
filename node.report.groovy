NUM_FIELDS = 11

class Node {
  String name = ''
  String description = ''
  String exectutors = ''
  String rootPath = ''
  String usage = ''
  String labels = ''
  String launcher = ''
  String host = ''
  String offline = ''
  String clock = ''
  String availability = ''
  
  Node(name, description, exectutors, rootPath, usage, labels, launcher, host, offline, availability, clock='') {
    this.name = name ? name : ''
    this.description = description ? description : ''
    this.exectutors = exectutors ? exectutors : ''
    this.rootPath = rootPath ? rootPath : ''
    this.usage = usage ? usage : ''
    this.labels = labels ? labels : ''
    this.launcher = launcher ? getActualLauncher(launcher.toString()) : ''
    this.host = host ? host : ''
    this.offline = offline ? offline : ''
    this.availability = availability ? getActualAvailability(availability.toString()) : ''
    this.clock = clock ? clock : ''
  }

  String getActualLauncher(input) {
    input = input.tokenize('@')[0]
    def parts = input.tokenize('.')
    def output = parts[parts.size - 1]
    output = output ? output : input
    return output
  }

  String getActualAvailability(input) {
    input = input.tokenize('@')[0]
    def output = input.tokenize('$')[1]
    output = output ? output : input
    return output
  }
}
class NodeList {
  List nodes = []
  
  int nameLength
  int descriptionLength
  int exectutorsLength
  int rootPathLength
  int usageLength
  int labelsLength
  int launcherLength
  int hostLength
  int offlineLength
  int clockLength
  int availabilityLength
  
  int totalLength() {
    return nameLength + descriptionLength + exectutorsLength + rootPathLength + 
    usageLength + labelsLength + launcherLength + hostLength + offlineLength + clockLength + availabilityLength
  }

  void add(Node node) {
    nameLength = Math.max(this.nameLength, node.name.length())
    descriptionLength = Math.max(this.descriptionLength, node.description.length())
    exectutorsLength = Math.max(this.exectutorsLength, node.exectutors.length())
    rootPathLength = Math.max(this.rootPathLength, node.rootPath.length())
    usageLength = Math.max(this.usageLength, node.usage.length())
    labelsLength = Math.max(this.labelsLength, node.labels.length())
    launcherLength = Math.max(this.launcherLength, node.launcher.length())
    hostLength = Math.max(this.hostLength, node.host.length())
    offlineLength = Math.max(this.offlineLength, node.offline.length())
    clockLength = Math.max(this.clockLength, node.clock.length())
    availabilityLength = Math.max(this.availabilityLength, node.availability.length())
    nodes.push(node)
    nodes = nodes.sort{it.name}
  }

  int length() {
    return nodes.size
  }

  Node getNodeAt(index) {
    return nodes[index]
  }
}
NodeList nodeList = new NodeList()
nodeList.add(new Node('Name', 'Description','Executors','Root Path','Usage','Labels','Launcher','Host','Offline', 'Availability', 'Clock'))
def node = null
for (aSlave in hudson.model.Hudson.instance.slaves) {
  node = new Node(
    aSlave.name,
    aSlave.getNodeDescription(),
    aSlave.getNumExecutors(),
    aSlave.getRootPath(),
    aSlave.mode,
    aSlave.getLabelString(),
    aSlave.getComputer().launcher,
    aSlave.getComputer().hostName,
    aSlave.getComputer().isOffline().toString(),
    aSlave.retentionStrategy
  )
  if (aSlave.getChannel()) {
    node.clock = aSlave.clockDifference
  } else {
    node.clock = 'offline'
  }
  nodeList.add(node)
}

if (nodeList.length() == 0) {
  println 'No nodes'
  return
}
println "-".multiply(nodeList.totalLength() + NUM_FIELDS)
row = ""
for (int i =0; i < nodeList.length(); i++) {
  row = ""
  node = nodeList.getNodeAt(i)
  row += node.name.padRight(nodeList.nameLength) + "|"
  row += node.description.padRight(nodeList.descriptionLength) + "|"
  row += node.exectutors.padRight(nodeList.exectutorsLength) + "|"
  row += node.rootPath.padRight(nodeList.rootPathLength) + "|"
  row += node.usage.padRight(nodeList.usageLength) + "|"
  row += node.labels.padRight(nodeList.labelsLength) + "|"
  row += node.launcher.padRight(nodeList.launcherLength) + "|"
  row += node.host.padRight(nodeList.hostLength) + "|"
  row += node.offline.padRight(nodeList.offlineLength) + "|"
  row += node.availability.padRight(nodeList.availabilityLength) + "|"
  row += node.clock.padRight(nodeList.clockLength) + "|"
  println row
}
println "-".multiply(nodeList.totalLength() + NUM_FIELDS)

