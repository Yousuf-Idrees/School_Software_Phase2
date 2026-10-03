import sqlite3

db_path = "school.db"

try:
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # Get all tables
    cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
    tables = cursor.fetchall()
    
    print("=" * 60)
    print("DATABASE TABLES IN school.db")
    print("=" * 60)
    
    if tables:
        for table in tables:
            table_name = table[0]
            
            # Get row count
            cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
            count = cursor.fetchone()[0]
            
            print(f"\nTable: {table_name} ({count} rows)")
            
            # Get schema
            cursor.execute(f"PRAGMA table_info({table_name})")
            columns = cursor.fetchall()
            print("-" * 60)
            for col in columns:
                print(f"  - {col[1]} ({col[2]})")
            
            # Show sample data
            if count > 0:
                cursor.execute(f"SELECT * FROM {table_name} LIMIT 3")
                rows = cursor.fetchall()
                print(f"Sample data (first 3 rows):")
                for row in rows:
                    print(f"  {row}")
    else:
        print("NO TABLES FOUND!")
        print("Database appears to be empty.")
    
    conn.close()
    print("\n" + "=" * 60)
    print("Database verification complete!")
    print("=" * 60)
    
except sqlite3.Error as e:
    print(f"Error: {e}")
except Exception as e:
    print(f"Unexpected error: {e}")
