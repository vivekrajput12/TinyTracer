# 1. Go to the folder where you want the project
cd /path/where/you/want/project

# 2. Clone your GitHub repo
git clone https://github.com/vivekrajput12/TinyTracer.git

# 3. Enter the project folder
cd TinyTracer

# 4. Build the project with Maven (downloads dependencies and compiles)
mvn clean install

# 5. Run your main class
mvn exec:java -Dexec.mainClass="com.yourpackage.MainClass"
