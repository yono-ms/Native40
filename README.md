# Native40
Native Android Application.

# Data Binding

- ViewレベルはFragmentで完結させる
- なんでもbindしない
  - RecyclerAdapterは必要機能を整理してonBindViewHolderをコードで書く
  - ListAdapterでDiffUtilを使うとLiveDataから変更点だけ適用できる
- onClickListenerなどイベントはxmlに書かない
  - inflateのalsoでit.button.setOnClickListener
  - ここでviewModel.onClick(activityViewModel)などでVMに切り替える

# View Model

- Viewプロパティなどの要素は使わず抽象化する
- 全部がMutableLiveDataではない
- ObservableListは使わずにListAdapterで実現する
  - 見た目だけソートはソートキーとDBのLiveDataからMediatorLiveDataを作る
