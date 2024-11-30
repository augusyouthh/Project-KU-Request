# CS211 Project ภาคต้น 2567

## ชื่อทีม monday-i-miss-you

### สมาชิกในทีม
|  รหัสนิสิต  |    ชื่อ-นามสกุล (ชื่อเล่น)     |  GitHub username  |
|:-----------:|:------------------------------:|:-----------------:|
|  6610450170 |  ณัฐธีร์ พรศตทวีวัฒน์ (ออกัส)  |    augusyouthh    |
| 6610450323  |      พีรณัฐ เผ่าผม (เฟรม)      |   Peeranat1908    |
| 6610451044  |     ภูดิส คู่ตระกูล (พีท)      |    PetePoodit     |
| 6610451036  |     ภวัต หวังศิริสุข (นิค)     |      PawatW       |

## คลิปความก้าวหน้าของระบบ
| ครั้งที่                      |       กำหนดส่ง        | Youtube Link |
|-------------------------------|:---------------------:|--|
| ความก้าวหน้าของระบบครั้งที่ 1 | 9 ส.ค. 2567 17:00 น.  | https://www.youtube.com/watch?v=27ETe45RVfM&feature=youtu.be |
| ความก้าวหน้าของระบบครั้งที่ 2 | 6 ก.ย. 2567 17:00 น.  | https://www.youtube.com/watch?v=DR3s_1SLV-c |
| ความก้าวหน้าของระบบครั้งที่ 3 | 27 ก.ย. 2567 17:00 น. | https://youtu.be/r-sYnMrtUOA?si=-iCTnWznAbjHLi9M |
| โครงงานที่สมบูรณ์             | 18 ต.ค. 2567 17:00 น. | https://youtu.be/HXJZtx3EOvY |


## สรุปสิ่งที่พัฒนาในแต่ละครั้ง
### ความก้าวหน้าของระบบครั้งที่ 1
1. ณัฐธีร์ พรศตทวีวัฒน์ (ออกัส)
   * สร้างหน้า register สำหรับและนิสิต
   * สร้างปุ่มเชื่อมโยง2ปุ่มได้แก่ ปุ่มRegister จะเชื่อมโยงไปหาหน้าถัดไปคือหน้าmainของนิสิต และปุ่มlogin ที่จะกลับเข้าไปหาหน้าloginในกรณีที่นิสิตเคยลงทะเบียนมาแล้ว
2. พีรณัฐ เผ่าผม (เฟรม)
   * ทำหน้า first page เพื่อที่จะเชื่อมไปยังหน้า login page เพื่อให้หน้า user interface มีความน่าใช้มากขึ้น
   * ออกแบบหน้า ui วางโครงสร้างของ application ของทุกๆหน้า เพื่อที่จะทำไปใช้ในการ develop ต่อไปในอนาคต
   * ทำหน้า my team เพื่อแสดงข้อมูลของทีม และ ทำปุ่มเชื่อมกลับไปที่หน้า student
3. ภูดิส คู่ตระกูล (พีท)
   * สร้างหน้า main ของนิสิตเบื้องต้นผ่าน scene builder
   * วางรูปเเบบการใช้งานใน figma ไว้แล้วว่าส่วนประกอบมีอะไรบ้าง เรื่องของปุ่มและการใช้งานปุ่มแต่ละอัน
   * design หน้า my team เเสดงข้อมูลของสมาชิกในกลุ่มเเต่คน
4. ภวัต หวังศิริสุข (นิค)
    * สร้างหน้าlogin.fxml ผ่าน scene builder
    * ทำปุ่มเชื่อมไปยัง registerในกรณีที่นิสิตยังไม่ได้ลงทะเบียน
    * ปุ่ม login เชื่อมไปยังหน้าstudent ในเบื้องต้น

### ความก้าวหน้าของระบบครั้งที่ 2
1. ณัฐธีร์ พรศตทวีวัฒน์ (ออกัส)
    * ทำระบบลงทะเบียน(ระบบregister)สำหรับนิสิตจนเกือบสมบูรณ์สามารถWriteข้อมูลลงไฟล์CSVได้
    *และได้สร้างหน้าMainAdminเป็นหน้าหลักของระบบฝ่ายผู้ดูแลระบบ ในอนาคตจะมีฟังก์ชั่นที่สามารถแก้ไขข้อมูลอาจารย์-เจ้าหน้าที่ได้ครับ
2. ภวัต หวังศิริสุข (นิค)
    * ทำระบบ login เช็คusername,password,บทบาทในไฟล์ได้
    * ทำระบบ reset password แก้ไข password ในไฟล์ได้
    * สร้างหน้า faculty-data-Admin-page
3. ภูดิส คู่ตระกูล (พีท)
    * เพิ่มเติมในส่วนระบบของนิสิต มีหน้าเพิ่มคำร้องทั่วไปเพิ่มขึ้น student-appeal เเละ normal-appeal
    * สร้าง Model ของคำร้องทั่วไปเเล้ว(NormalAppeal , NormalAppealList) เเละจะทำการ inherit คำร้องประเภทอื่นในอนาคต
    * ในส่วนของหน้า normal-appeal สามารถเขียนเเละกดส่งข้อมูลคำร้องได้เเล้วเเต่จะเพิ่มให้สามารถโชว์เเละติดตามคำร้องได้ในอนาคต

4. พีรณัฐ เผ่าผม (เฟรม)
    * เพิ่มหน้า advisor มี table view สำหรับแสดงรายชื่อนิสิต
    * เพิ่มหน้ารายการคำร้องสำหรับแสดงคำร้องล่าสุดที่นักเรียนส่งมา


### ความก้าวหน้าของระบบครั้งที่ 3
1. ณัฐธีร์ พรศตทวีวัฒน์ (ออกัส)
    * ทำหน้าUserProfileและส่งข้อมูลUserข้ามหน้า
    * เริ่มทำหน้าดูนิสิตในภาค(งานของเจ้าหน้าที่ภาค)
2. ภวัต หวังศิริสุข (นิค)
   * ทำหน้าแสดงผู้ใช้บัญชีทั้งหมด สามารถค้นหาตามข้อมูลผู้ใช้ได้และสามารถกรองตามบทบาทของบัญชีผู้ใช้ได้ 
     สามารถกดเข้าไปเพื่อดูข้อมูลของผู้ใช้ได้และสามารถระงับหรือปลดระงับบัญชีผู็ใช้ได้
   * ทำหน้าการจัดการเจ้าหน้าที่และอาจารย์โดยสามารถค้นหาตามข้อมูลและสามารถกดเข้าไปดูข้อมูลของบัญชีนั้นได้
     ในหน้าดูข้อมูลของเจ้าหน้าที่และอาจารย์สามารถแก้ไขข้อมูลบัญชีได้
   * ทำระบบเพิ่มข้อมูลเจ้าหน้าที่และอาจารย์สามารถเลือกตามบทบาทที่จะเพิ่มได้
3. ภูดิส คู่ตระกูล (พีท)
     * ระบบคำร้องมีครบ 3 ประเภทเเล้วประกอบด้วย  คำร้องทั่วไป, ใบลาพักการศึกษา , ขอลงทะเบียนเรียน
     * สามารถส่งคำร้องได้หมดทุกอันเเละเเสดงคำร้องที่เคยส่งไปได้ โดยสามารถกดปุ่มเพื่อเลือกประเภทของคำร้องที่จะต้องการจะดูได้
       เเละยังสามารถ search หาคำร้องได้จากหัวเรื่องหรือรายละเอียด เเละเรียงลำดับคำร้องตามที่ส่งล่าสุดเเล้ว รวมทั้งสามารถกดดูรายละเอียดคำร้องทั้งหมด
       โดยการกดที่กล่องคำร้องเเต่ละอันที่เเสดงได้เลย
     * ตัวของคำร้องที่เเสดงในหน้าติดตามคำร้องจะขึ้นตามของนักเรียนที่ Login เข้ามาใช้งาน
4. พีรณัฐ เผ่าผม (เฟรม)
   * ทำหน้า admin ในส่วนของข้อมูลคณะและสาขา สามารถแสดงข้อมูลคณะทั้งหมดได้และสามารถแยกไปตามสาขาได้และสามารถที่จะเพิ่มข้อมูลหรือแก้ไขข้อมูลคณะกับสาขาได้
   * ทำหน้า advisor ในส่วนของรายชื่อนิสิตในอาจารย์ที่ปรึกษา สามารถแสดงชื่อนิสิตทั้งหมดและสามารถค้นหานิสิตได้ผ่าน ชื่อ และ รหัสนิสิต
   * ทำหน้า faculty staff สามารถแสดงรายการคำน้องของนิสิตและสามารถแสดงข้อมูลผู้อนุมัติคำร้อง โดยสามารถแก้ไขและเพิ่มข้อมูลของผู้อนุมัติได้

### โครงงานที่สมบูรณ์
1. ณัฐธีร์ พรศตทวีวัฒน์(ออกัส)
    * การอัพโหลดและดาวน์โหลดไฟล์PDFที่มีลายเซ็นของผู้อนุมัติคำร้องทั้งคณะและภาควิชา
    * ทำระบบอนุมัติที่มีความละเอียดของเจ้าหน้าที่ภาควิชาทำให้การทำงานมีช่องโหว่น้อย
    * การสร้างระบบที่ใช้การต่อไฟล์ในmajor-endorser.csv ทำให้โปรแกรมมีรูปแบบการที่น้อยลงและประหยัดเวลาการทำงาน
    * การสร้างระบบเพิ่ม-แก้ไขข้อมูลนิสิตของเจ้าหน้าที่าภาควิชาทำให้ระบบมีความเป็นยระเบียบเมื่อมีนิสิตทำหารลงทะเบียนครั้งแรก
    * รูปแบบและขั้นตอนการpassหรือส่งไฟล์และคำร้องอย่างมีระบบของเจ้าหน้าที่ภาควิชาให้กับเจ้าหน้าที่คณะ ทำให้การทำงานอยู่ในรูปแบบที่คล้ายกัน และเข้าใจง่ายในตอนนี้ผมได้รับผิดชอบในส่วนของmajor-staffครบถ้วนแล้ว
   
2. ภวัต หวังศิริสุข (นิค)
    * รับผิดชอบการจัดการข้อมูลผู้ใช้โดยรวม การเพิ่มข้อมูลหรือแก้ไขuserทั้งหมด ในระบบของผู้ดูแลและการเพิ่มนิสิตและแก้ไขในระบบของเจ้าหน้าที่ภาค
    * รับผิดชอบระบบการล็อกอิน ลงทะเบียนและการเปลี่ยนรหัสผ่าน
    * สร้างหน้าdashboard ดูภาพรวมและรายละเอียดข้อมูลผู้ใช้และคำร้องภายในโปรแกรม
    * รับผิดชอบหน้าโปรไฟล์ในการเปลี่ยนรูปภาพและรหัสผ่านผ่านหน้า โปรไฟล์ผู้ใช้
3. ภูดิส คู่ตระกูล (พีท)
    * รับผิดชอบในส่วนของหน้านิสิต โดยสามารถส่งคำร้องได้ครบ 3 ประเภทเเละสามารถติดตามรายการคำร้องได้อย่างถูกต้อง เเละมีข้อมูลขึ้นครบถ้วน
    * รับผิดชอบในส่วนของหน้าอาจารย์ที่ปรึกษา มีรายชื่อนิสิตที่อยู่ในที่ปรึกษาขึ้นมาอย่างถูกต้อง เเละสามารถกดเข้าไปดูคำร้องของนิสิตเเต่ละคนได้ พร้อมทั้งสามารถ กดอนุมัติ เเละ ปฎิเสธคำร้องได้อย่างถูกต้องเเละครบถ้วน เมื่อกดเสร็จจะเปลี่ยนสถานะเเละส่งให้กับเจ้าหน้าที่ต่อๆไป
    * ช่วยเหลือในส่วนของระบบของคำร้องในหน้าของ เจ้าหน้าที่ภาคเเละเจ้าหน้าที่คณะด้วยเช่นกัน ในเรื่องของการขึ้นข้อมูลของคำร้องโดยกรองจาก คณะ หรือ ภาค ของเจ้าหน้าที่ได้อย่างถูกต้องเเละครบถ้วน เสร็จสมบูรณ์
4. พีรณัฐ เผ่าผม (เฟรม)
    * ระบบเจ้าหน้าที่คณะในการรับคำร้อง กดอนุมัติ ปฎิเสธ อัพโหลดไฟล์ pdf
    * ทำหน้าข้อมูลผู้อนุมัติคำร้อง โดยให้เรียงจากคณะที่ user สังกัดอยู่เท่านั้น
    * หน้าแก้ไขข้อมูลผู้อนุมัติคำร้อง และหน้าเพิ่มข้อมูลผู้อนุมัติคำร้อง
    * ทำหน้าข้อมูลผู้อนุมัติคำร้อง ของเจ้าหน้าที่ภาควิชาให้เรียงตามสาขาที่สังกัดอยู่
    * ทำแถบ sidebar ด้านข้างโดยจะมีอยู่ในทุกๆหน้า ให้ทำการแสดงขึ้นมาเมื่อกดปุ่มและสามารถกดออกจากระบบได้ กดเข้าหน้า developer ได้
    * แก้ไข ui ให้ทุกๆหน้ามีความสวยงามและเพิ่มลูกเล่นให้แอพพลิเคชั่น

## วิธีการติดตั้งและรันโปรแกรม
   โปรแกรมที่สมบูรณ์ จะอยู่ใน Directory Release สามาถย้ายไปไว้ใน directory ที่ต้องการแล้ว
   กด ที่ jar file 
   เพื่อเปิดโปรแกรมได้เลย
    หรือใช้ command line tool เพื่อ change directory (cd) ไปที่ directory นั้น
    แล้วใช้คำสั่ง java -jar MYMEE.jar

## ตัวอย่างข้อมูลผู้ใช้ระบบ (username, password) 
Admin
- Username: Admin
- Password: 01418211

อาจารย์ที่ปรึกษา
- Username: thanapol.s
- Password: thanapol456


- Username: krummun.c
- Password: krummun123


- Username: thammakorn.k
- Password: thamma123


- Username: pimphorn.s
- Password: pimphorn456


- Username: jiraporn.s
- Password: jiraporn123 



เจ้าหน้าที่ภาควิชา
- Username: witaya.i
- Password: wit7890


- Username: somchai.s
- Password: som1234


- Username: napassorn.k
- Password: napat123


- Username: naraseth.a
- Password: nara1234


- Username: monthira.c
- Password: month1234


- Username: songphon.n
- Password: song1234


- Username: manop.y
- Password: mana5678


เจ้าหน้าที่คณะ
- Username: kreangkrai_eng
- Password: kreE@456


- Username: usa_sammapan
- Password: thanS@456


- Username: thanakorn_sci
- Password: thamma123


- Username: prasert_busi
- Password: praB@789


- Username: sakchai_econ
- Password: sakE@789


นิสิต
- Username: b661021
- Password: 661021


- Username: b661023
- Password: 661023


- Username: b661016
- Password: 661016


- Username: b661018
- Password: 661018


- Username: b661031
- Password: 661031


- Username: b661036
- Password: 661036


- Username: b661038
- Password: 661038


- Username: b661001
- Password: 661001


- Username: b661015
- Password: 661015


- Username: b661010
- Password: 661010
   

## การวางโครงสร้างไฟล์ของโครงงาน
- **data** (เก็บไฟล์ CSV และข้อมูลอื่น ๆ)  
  ├── **appealPDF** (เก็บไฟล์ PDF เกี่ยวกับคำร้อง)
  ├──**UserProfileImage** (เก็บรูปโปรไฟล์ของผู้ใช้งาน)  
  ├── **appeals.csv** (ไฟล์ CSV เกี่ยวกับคำร้อง)  
  ├── **approveFacultyStaff.csv** (ไฟล์ CSV ผู้อนุมัติระดับคณะ)

  ├──**majorOrder.csv** (ไฟล์ CSV ผู้อนุมัติระดับภาควิชา)

  ├──**student-info.csv** (ไฟล์ CSV ข้อมูลนักศึกษา)
  ├──**studentPreRegister.csv** (ไฟล์ CSV ข้อมูลนักศึกษาที่เจ้าหน้่ที่ภาคเพิ่มมา)

  ├──**user.csv** (ไฟล์ CSV ข้อมูลผู้ใช้)

  ├──**Faculty.csv** (ไฟล์ CSV ข้อมูลคณะ)

  ├──**Major.csv** (ไฟล์ CSV ข้อมูลภาควิชา)

  └──**วิธีการใช้งานMYMEE.pdf** (ไฟล์ PDF รายงานโปรเจกต์)
- **Release** (เก็บไฟล์สำหรับ Release)  
  └── **data** (เก็บไฟล์ CSV และข้อมูลอื่นๆ ที่ใช้ในโปรแกรม)

    └── **UML** (เก็บไฟล์รูปUML)

- **src.main**
  - **java.ku.cs**

    └── **cs21167project** 

    ├── **controllers** (เก็บไฟล์คอนโทรลเลอร์)  
    ├── **services** (เก็บไฟล์datasource,comparatorและอื่นๆ)  
    └── **models** (เก็บโมเดล)  

  - **resources** (เก็บไฟล์ทรัพยากร)  
    ├── **images** (เก็บรูปในโปรแกรม)
        - **java.ku.cs**
         ├── **views** (เก็บไฟล์fxml)  
         ├── **styles.css** (ไฟล์ CSS สำหรับสไตล์การแสดงผล)  
         ├── **fonts** (เก็บฟอนต์)  
         └── **NotoSansThai.ttf** (ไฟล์ฟอนต์ภาษาไทย)  

รายละเอียดของไฟล์ CSV
ข้อมูลส่วนรวมจะถูกเก็บไว้ที่ directory ‘data’ 
ได้แก่ 
- user.csv 
  - index ที่ 0 = ชือบัญชีผู้ใช้ 
  - -index ที่ 1 = รหัสผ่าน 
  - index ที่ 2 = ชื่อผู้ใช้ 
  - index ที่ 3 = วันที่เข้าล็อกอินล่าสุด 
  - index ที่ 4 = เวลาที่ล็อกอินล่าสุด 
  - index ที่ 5 = บทบาทบัญชีผู้ใช้(แอดมิน,นิสิต,เจ้าหน้าที่ภาควิชา,เจ้าหน้าที่คณะ,อาจารย์ที่ปรึกษา) 
  - index ที่ 6 = ที่อยู่ไฟล์รูปภาพโปรไฟล์ผู้ใช้ 
  - index ที่ 7 = สถานะบัญชีผู้ใช้ 
  - index ที่ 8 = คณะ 
  - index ที่ 9 = ภาควิชา 
  - index ที่ 10 = สถานะการเข้าล็ิกอินครั้งแรก 
  - index ที่11 =รหัสประจำตัว(นิสิต,อาจารย์ที่ปรึกษา)
- student-info.csv 
  - index ที่ 0 = ชือบัญชีผู้ใช้ 
  - index ที่ 1 = ชื่อผู้ใช้ 
  - index ที่ 2 = รหัสนิสิต 
  - index ที่ 3 = Email 
  - index ที่ 4 = คณะ 
  - index ที่ 5 = ภาควิชา 
  - index ที่ 6 = รหัสอาจารย์ที่ปรึกษา
- studentPreRegister.csv 
  - index ที่ 0 = ชื่อผู้ใช้ 
  - index ที่ 1 = รหัสนิสิต 
  - index ที่ 2 = Email 
  - index ที่ 3 = คณะ 
  - index ที่ 4 = ภาควิชา 
  - index ที่ 5 = รหัสอาจารย์ที่ปรึกษา 
- major-endorser.csv 
  - index ที่ 0 = ชื่อ-นามสกุลของผู้มีสิทธิ์อนุมัติ 
  - index ที่ 1 = ตำแหน่งพร้อมภาควิชาและคณะที่สังกัด
- approveFacultyStaff.csv 
  - index ที่ 0 = ชื่อ-นามสกุลของผู้มีสิทธิ์อนุมัติ 
  - index ที่ 1 = ตำแหน่งพร้อมภาควิชาและคณะที่สังกัด 
  - index ที่ 2 = คณะที่สังกัด
- Major.csv 
  - index ที่ 0 = รหัสคณะ 
  - index ที่ 1 = รหัสภาควิชา 
  - index ที่ 2 = ชื่อภาควิชา 
- Faculty.csv 
  - index ที่ 0 = รหัสคณะ 
  - index ที่ 1 = ชื่อคณะ 
- appeals.csv
  - index ที่ 0 = ID ของนักเรียนที่ส่งคำร้อง 
  - index ที่ 1 =  ชนิดหรือประเภทของคำร้อง 
  - index ที่ 2 =  หัวข้อของคำร้อง 
  - index ที่ 3 =  รายละเอียดของคำร้อง 
  - index ที่ 4 =  วันที่ส่งคำร้อง 
  - index ที่ 5 =  ลายเซ็นของนิสิตหรือผู้ลงนามเเทน 
  - index ที่ 6 = Timestamp (ไว้สำหรับ sort ว่าคำร้องไหนถูกจัดการก่อนหรือหลัง) 
  - index ที่ 7 = สถานะของคำร้อง 
  - index ที่ 8 = เวลาที่ส่งคำร้อง 
  - index ที่ 9 = เหตุผลที่ปฎิเสธ (ถูกบันทึกจากอาจารย์ที่ปรึกษาหรือเจ้าหน้าที่ก่อนจึงจะเห็น) 
  - index ที่ 10 = ลายเซ็นผู้อนุมัติของเจ้าหน้าที่ภาค (ถูกบันทึกจากเจ้าหน้าที่ภาคก่อนจึงจะเห็น) 
  - index ที่ 11 = วันที่อนุมัติของเจ้าหน้าที่ภาค (ถูกบันทึกจากเจ้าหน้าที่ภาคก่อนจึงจะเห็น) 
  - index ที่ 12 = วันที่อนุมัติของเจ้าหน้าที่คณะ ถูกบันทึกจากเจ้าหน้าที่คณะก่อนจึงจะเห็น) 
  - index ที่ 13 = วันเวลาที่ถูกปฎิเสธ (ถูกบันทึกจากอาจารย์ที่ปรึกษาหรือเจ้าหน้าที่ก่อนจึงจะเห็น) 
  - index ที่ 14 = ลายเซ็นผู้อนุมัติของเจ้าหน้าที่คณะ(ถูกบันทึกจากเจ้าหน้าที่คณะก่อนจึงจะเห็น) 
  - index ที่ 15 = ID ของคำร้อง (สำหรับเเบ่งเเยกคำร้องเเต่ละในการเปิดดูไฟล์ pdf) 
  - index ที่ 16 = path ของไฟล์ pdf 

   