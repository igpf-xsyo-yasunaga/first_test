# インターフェース仕様書

## 1. インターフェース概要

| 項目 | 内容 |
|------|------|
| インターフェース名 | 顧客データ同期API |
| 接続方式 | RESTful API |
| 通信プロトコル | HTTPS |
| 接続先情報 | URL: https://api.example.com/v1/customers <br> ポート番号: 443 |
| 認証方式 | Bearer Token認証 |
| トリガー方式 | プル型（定期バッチ処理） |
| 入出力の方向 | 送信／受信（双方向） |

## 2. リクエスト仕様

### 2.1 顧客データ取得 API

| 項目 | 内容 |
|------|------|
| HTTPメソッド | GET |
| エンドポイント | /api/v1/customers |
| ヘッダ情報 | Content-Type: application/json <br> Authorization: Bearer {token} |
| クエリパラメータ | last_updated_since: YYYY-MM-DDThh:mm:ss (オプション) <br> limit: 数値 (デフォルト: 100) <br> offset: 数値 (デフォルト: 0) |

### 2.2 顧客データ登録 API

| 項目 | 内容 |
|------|------|
| HTTPメソッド | POST |
| エンドポイント | /api/v1/customers |
| ヘッダ情報 | Content-Type: application/json <br> Authorization: Bearer {token} |
| リクエストボディ | JSONフォーマット（詳細は「3. データ項目一覧」参照） |

### 2.3 顧客データ更新 API

| 項目 | 内容 |
|------|------|
| HTTPメソッド | PUT |
| エンドポイント | /api/v1/customers/{customer_id} |
| ヘッダ情報 | Content-Type: application/json <br> Authorization: Bearer {token} |
| リクエストボディ | JSONフォーマット（詳細は「3. データ項目一覧」参照） |

## 3. データ項目一覧

### 3.1 リクエストデータ項目

| 項目名 | 型 | 必須/任意 | 桁数 | 説明 |
|--------|-----|----------|------|------|
| customer_id | 文字列 | 必須 | 20 | 顧客ID |
| name | 文字列 | 必須 | 100 | 顧客名 |
| email | 文字列 | 必須 | 256 | メールアドレス |
| phone | 文字列 | 任意 | 15 | 電話番号 |
| address | オブジェクト | 任意 | - | 住所情報 |
| address.postal_code | 文字列 | 必須（addressが存在する場合） | 8 | 郵便番号 |
| address.prefecture | 文字列 | 必須（addressが存在する場合） | 10 | 都道府県 |
| address.city | 文字列 | 必須（addressが存在する場合） | 50 | 市区町村 |
| address.street | 文字列 | 必須（addressが存在する場合） | 100 | 町名・番地 |
| address.building | 文字列 | 任意 | 100 | 建物名・部屋番号 |
| created_at | 日時 | 必須 | - | 作成日時（ISO 8601形式） |
| updated_at | 日時 | 必須 | - | 更新日時（ISO 8601形式） |
| status | 文字列 | 必須 | 20 | ステータス（"active", "inactive", "suspended"） |

### 3.2 レスポンス仕様

#### 3.2.1 正常系レスポンス

| ステータスコード | 説明 |
|------------------|------|
| 200 OK | リクエスト成功（GET, PUT） |
| 201 Created | リソース作成成功（POST） |

#### 3.2.2 GET レスポンスフォーマット

```json
{
  "data": [
    {
      "customer_id": "CUS00001",
      "name": "山田太郎",
      "email": "yamada@example.com",
      "phone": "090-1234-5678",
      "address": {
        "postal_code": "100-0001",
        "prefecture": "東京都",
        "city": "千代田区",
        "street": "大手町1-1-1",
        "building": "サンプルビル101"
      },
      "created_at": "2025-01-15T09:30:00+09:00",
      "updated_at": "2025-04-20T14:15:30+09:00",
      "status": "active"
    },
    // 他の顧客データ...
  ],
  "pagination": {
    "total": 1500,
    "limit": 100,
    "offset": 0,
    "next_offset": 100
  }
}
```

#### 3.2.3 POST/PUT レスポンスフォーマット

```json
{
  "customer_id": "CUS00001",
  "name": "山田太郎",
  "email": "yamada@example.com",
  "phone": "090-1234-5678",
  "address": {
    "postal_code": "100-0001",
    "prefecture": "東京都",
    "city": "千代田区",
    "street": "大手町1-1-1",
    "building": "サンプルビル101"
  },
  "created_at": "2025-01-15T09:30:00+09:00",
  "updated_at": "2025-04-20T14:15:30+09:00",
  "status": "active"
}
```

## 4. エラーハンドリング

### 4.1 エラーレスポンス

| ステータスコード | 説明 |
|------------------|------|
| 400 Bad Request | リクエストパラメータエラー |
| 401 Unauthorized | 認証エラー |
| 403 Forbidden | アクセス権限エラー |
| 404 Not Found | リソースが存在しない |
| 409 Conflict | 重複エラー（既存データとの競合） |
| 429 Too Many Requests | リクエスト制限超過 |
| 500 Internal Server Error | サーバー内部エラー |

### 4.2 エラーレスポンスフォーマット

```json
{
  "error": {
    "code": "E1001",
    "message": "リクエストパラメータが不正です",
    "details": [
      {
        "field": "email",
        "message": "有効なメールアドレス形式で入力してください"
      }
    ]
  }
}
```

## 5. トランザクション処理

| 項目 | 内容 |
|------|------|
| トランザクション境界 | 各APIリクエスト単位でトランザクションを管理 |
| ロールバック条件 | データベースエラー発生時、すべての更新処理をロールバック |
| エラー時の再試行 | 最大3回まで自動再試行。以降は手動対応 |

## 6. ファイル仕様

本インターフェースでは直接のファイル連携は行わない。（データはすべてJSON形式で送受信）

## 7. 想定件数・サイズ

| 項目 | 内容 |
|------|------|
| 想定レコード件数 | 最大10,000件/日 |
| データ送受信サイズ | 平均2KB/レコード、最大20MB/リクエスト |
| レスポンスタイム | 通常：1秒以内、大量データ取得時：最大30秒 |

## 8. 通信タイミング

| 項目 | 内容 |
|------|------|
| バッチ処理 | 毎日3:00 AMに前日更新分データを同期 |
| リアルタイム処理 | 顧客情報更新時にWebhookによる通知 |
| 接続頻度制限 | 1分あたり最大100リクエストまで |

## 9. 備考

* 本番環境への接続には専用のVPN経由でのアクセスが必要
* 大量データ同期の場合はバッチウィンドウ（1:00 AM - 5:00 AM）内での実行を推奨
* 認証トークンの有効期限は24時間、期限切れ前に更新処理が必要
* 障害発生時の連絡先：system-admin@example.com（24時間対応）