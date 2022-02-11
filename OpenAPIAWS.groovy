import groovy.yaml.YamlSlurper

def writeYaml(String openApiUrl, String baseUrl, String outputFile){
    def openapi = new URL(openApiUrl).text
    def yamlSlurper = new YamlSlurper()
    def yamlOrig = yamlSlurper.parseText(openapi)

    yamlOrig.paths.each { pathStr, config ->
        config.each { httpMethod, methodConfig ->
            methodConfig.'x-amazon-apigateway-integration' = [
                    "payloadFormatVersion": "1.0",
                    "type": "http_proxy",
                    "httpMethod": "${httpMethod.toUpperCase()}",
                    "uri": baseUrl + pathStr,
                    "connectionType": "INTERNET",
                    "timeoutInMillis": 30000
            ]
        }
    }
    yamlOrig.'x-amazon-apigateway-importexport-version' = "1.0"

    def yaml = new groovy.yaml.YamlBuilder()
    def result = yaml(yamlOrig)
    File file = new File(outputFile)
    file << yaml.toString()
}

//writeYaml("https://collections-test.ala.org.au/static/openapi.yml", "https://collections-test.ala.org.au/ws", "collectory-openapi.yaml")
writeYaml("https://bie-ws-test.ala.org.au/ws/static/openapi.yml", "https://bie-ws-test.ala.org.au/ws", "bie-openapi.yaml")
//writeYaml("http://dev.ala.org.au:8080/v3/api-docs.yaml", "https://biocache-ws-test.ala.org.au/ws", "biocache-openapi.yaml")
writeYaml("https://biocache-ws-test.ala.org.au/ws/v3/api-docs.yaml", "https://biocache-ws-test.ala.org.au/ws", "biocache-openapi.yaml")

