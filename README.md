# Chabjai Royale 
(จับฉ่ายรอยัล จับใจ แชบชาย แล้วแต่จะเรียกเลย)

-------------------------------------------

โปรเจกต์ส่งวิชา Object Oriented Programming มมส
ส่งเสร็จเรียบร้อยแล้ว ไม่รู้จะยังไงต่อ เสียดายเวลา แจกไปเลยละกัน

Download : https://github.com/PonlawatP/Chabjai_Royale/releases

โปรเจกต์นี้ใช้ Java 19 ในการรัน (แนะนำ OpenJDK 19 https://jdk.java.net/19/)

-------------------------------------------

ตัวเกมจะเน้นเล่นกันแบบ LAN (กลุ่มเล็กๆ) ถ้าถามว่าสามารถเล่นแบบ ip ได้มั้ย
*ได้* แต่ก็จะปวดหัวไปกับการตั้งค่าที่ทุกคนจะต้องไปหาวิธีกันเอาเองนะ ขี้เกียจทำวิธี

-------------------------------------------

Arguments ที่มี

- skip_intro
ข้าม intro ตอนเปิดเกม

- username=ชื่อ
กำหนด username ไม่ต้องใส่เองตอนเริ่มโปรแกรม

- score_win=จำนวนการฆ่าสูงสุด
กำหนดในเกมว่าจะให้ผู้เล่นฆ่าถึงเท่าไหร่จึงจะชนะ
ค่าเริ่มต้น = 6

- allowed_start=จำนวนผู้เล่นขั้นต่ำ
กำหนดจำนวนผู้เล่นที่สามารถกดเริ่มเกมได้
ค่าเริ่มต้น = 3

- port=พอร์ทเกม
กำหนด port สำหรับการ join เกม
ค่าเริ่มต้น = 50394

ชุดคำสั่งด้านล่างใช้สำหรับการทำ Multicast ถ้าไม่เข้าใจการจัด ip เพื่อทำ Multicast ไม่ควรใช้
- mc_port=พอร์ทสำหรับ Join Group Multicast
ค่าเริ่มต้น = 4321
- mc_ip=ไอพีสำหรับ Join Group Multicast 
ค่าเริ่มต้น = 230.0.0.0

-------------------------------------------

วิธีการใช้ Arguments
หลังจากใส่ code run เรียบร้อยแล้ว เราก็จะใส่ชุดข้อความที่ต้องการลงหลังจากโค้ดไฟล์เกม

เช่น
java -jar <file> <args>
java -jar Jabchai_Royale.jar username=Pluto

ถ้าต้องการใส่หลายคำสั่งให้เว้นวรรคแล้วใส่คำสั่งต่อได้เลย

-------------------------------------------

เอาไปทำความเข้าใจกันเอาเองนะ code โคตรรก แต่อย่าเน้นก็อปวางนะ ยังไงก็ดูออก
