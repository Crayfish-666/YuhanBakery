import mysql.connector

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="123",
        database="yuhan_bakery"
    )
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT id, order_no, status, delivery_type, rider_id, total_amount FROM orders;")
    orders = cursor.fetchall()
    print(f"Total orders: {len(orders)}")
    for o in orders:
        print(o)
except Exception as e:
    print(f"Error: {e}")
