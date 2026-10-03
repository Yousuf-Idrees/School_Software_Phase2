cd "c:\Users\omars\Desktop\Soft\Software GUI\school software\school software"
$files = Get-ChildItem -Path src -Recurse -Filter "*.java"
javac -encoding UTF-8 -d target/classes -cp "src" ($files | ForEach-Object { $_.FullName })

# Test database initialization
Write-Host "Testing database initialization..."
java -cp "target/classes" -c "
import com.school.data.DatabaseConnection;
public class TestDB {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        DatabaseConnection.populateInitialData();
        System.out.println('Database initialized successfully!');
    }
}"

Get-Item school.db -ErrorAction SilentlyContinue
if ($?) {
    Write-Host "✓ Database file created: school.db"
} else {
    Write-Host "✗ Database file not found"
}
