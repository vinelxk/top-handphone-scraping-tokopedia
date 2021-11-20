# top-handphone-scraping-tokopedia

Top Handphone Scraping Tokopedia is tool for scraping top phone product on tokopedia

It uses Selenium.

## Requirement
* Java 1.8
* Maven
* [Chrome Driver](https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver)

## Project Build
```mvn clean install```

## JAR Build
```mvn clean compile assembly:single```

Then, JAR file can be found in target directory.

## Run
```java -jar {jar_file} [size]```

Parameter `size` is required.

CSV file will be provided with a name in the following format: `Product_<productType>_<timeInMillis>.csv`

