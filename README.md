# Native40
Native Android Application.

# Data Binding

- ViewレベルはFragmentで完結させる
- なんでもbindしない
  - RecyclerAdapterは必要機能を整理してonBindViewHolderをコードで書く
  - CollectionChanged系イベントを使う
- onClickListenerなどイベントはxmlに書かない
  - inlateのalsoでit.button.setOnClickListener
  - ここでviewModel.onClick(activityViewModel)などでVMに切り替える

# View Model

- Viewプロパティなどの要素は使わず抽象化する
- 全部がMutableLiveDataではない
  - ObservableListでRecyclerAdapterのコードビハインドと連携もある
