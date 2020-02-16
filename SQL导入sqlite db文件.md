#SQL文件导入sqlite数据库命令：
cat WxFileIndex-decrypt.sql | sqlite3 WxFileIndex-decrypt.db
sqlite3 EnMicroMsg-decrypt.db ".read EnMicroMsg.sql"

直接从桌面1,2快速导入
cat 1 | sqlite3 EnMicroMsg-decrypt.db
cat 2 | sqlite3 WxFileIndex-decrypt.db
