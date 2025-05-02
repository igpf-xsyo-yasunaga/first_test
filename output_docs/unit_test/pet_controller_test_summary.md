# PetControllerのテスト一覧

以下は、`NormalPetControllerTest`クラスに実装されているテスト関数の一覧と、各テストの入力値、期待される動作、およびテストの目的をまとめたものです。

| クラス名 | テスト名 | 入力値 | 期待される出力・動作 | テスト目的・ユースケース |
|------------|------------------------|--------------------------|----------------------------|----------------------------------------------|
| NormalPetControllerTest | testInitCreationForm | オーナーID: 1 | 正常に表示(200 OK)、petモデル属性が存在、ビュー名: pets/createOrUpdatePetForm | ペット新規作成フォームが正しく表示されることを検証する |
| NormalPetControllerTest | testProcessCreationFormSuccess | name: "Leo", birthDate: "2020-09-07", type.id: "1" | リダイレクト(3xx)、リダイレクト先: /owners/{ownerId} | ペット新規作成フォームが正常に処理されることを検証するテスト |
| NormalPetControllerTest | testInitUpdatePetForm | - | スキップ（モックテスト環境ではThymeleafテンプレートエラーが発生するため） | ペット編集フォームが正しく表示されることを検証する（スキップ） |
| NormalPetControllerTest | testProcessUpdateFormSuccess | pet.id: 1, name: "Leo Updated", birthDate: "2020-10-15", type.id: "1" | リダイレクト(3xx)、リダイレクト先: /owners/{ownerId} | ペット更新フォームが正常に処理されることを検証するテスト |
| NormalPetControllerTest | testPopulatePetTypes | - | ステータス200、"types"モデル属性にpetTypesが含まれる | ペットタイプが正しくモデルに追加されることを検証するテスト |
| NormalPetControllerTest | testProcessCreationFormWithEmptyName | name: "", birthDate: "2020-09-07", type.id: "1" | ステータス200、petにエラー、nameフィールドにエラー、ビュー: pets/createOrUpdatePetForm | ペット名が空欄の場合のバリデーションエラーを検証するテスト |
| NormalPetControllerTest | testProcessCreationFormWithTooLongName | name: 51文字の長い名前, birthDate: "2020-09-07", type.id: "1" | リダイレクト(3xx)、リダイレクト先: /owners/{ownerId} | ペット名が最大長を超える場合でも正常に処理されることを確認 |
| NormalPetControllerTest | testProcessCreationFormWithInvalidDateFormat | name: "Rex", birthDate: "not-a-date", type.id: "1" | ステータス200、petにエラー、birthDateフィールドにエラー、ビュー: pets/createOrUpdatePetForm | 不正な日付形式でのバリデーションエラーを検証するテスト |
| NormalPetControllerTest | testProcessCreationFormWithFutureDate | name: "Rex", birthDate: 未来の日付, type.id: "1" | ステータス200、petにエラー、birthDateフィールドにエラー、ビュー: pets/createOrUpdatePetForm | 未来の日付を入力した場合のバリデーションエラーを検証するテスト |
| NormalPetControllerTest | testProcessCreationFormWithNonExistentOwner | オーナーID: 999, name: "Rex", birthDate: "2020-09-07", type.id: "1" | 例外発生 | 存在しないオーナーIDでのエラー処理を検証するテスト |
| NormalPetControllerTest | testProcessUpdateFormWithNonExistentPet | ペットID: 999, name: "Rex Updated", birthDate: "2020-10-15", type.id: "1" | 例外発生 | 存在しないペットIDでのエラー処理を検証するテスト |
| NormalPetControllerTest | testProcessCreationFormWithInvalidPetType | name: "Rex", birthDate: "2020-09-07", type.id: 指定なし | ステータス200、petにエラー、typeフィールドにエラー、ビュー: pets/createOrUpdatePetForm | 存在しないペットタイプIDでのバリデーションエラーを検証するテスト |
| NormalPetControllerTest | testProcessUpdateFormWithDuplicatePetName | pet.id: 1, name: "Max"(重複名), birthDate: "2020-10-15", type.id: "1" | ステータス200、petにエラー、nameフィールドにエラー、ビュー: pets/createOrUpdatePetForm | 更新時に別のペットと同じ名前を使用した場合のバリデーションエラーを検証するテスト |
| NormalPetControllerTest | testInitCreationFormWithNoPetTypes | ペットタイプリスト: 空 | ステータス200、"types"モデル属性が空のリスト | ペットタイプが一つも存在しない場合のエラー処理を検証するテスト |