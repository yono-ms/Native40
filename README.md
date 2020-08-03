# Native40
Native Android Application.

# Data Binding

- ViewレベルはFragmentで完結させる
- なんでもbindしない
  - RecyclerAdapterは必要機能を整理してonBindViewHolderをコードで書く
  - ListAdapterでDiffUtilを使うとLiveDataから変更点だけ適用できる
  - Item型を定義、MutableLiveData<List<Item>>をobserveしてsubmitList
  - VM側ではclearではなく（全クリアならnullをセット、）新リストをセットで更新する
  - DiffUtilが動いて差分だけアニメーション反映される
  - Item型はitem.xmlにバインド
    - ただしlifecycleOwnerは無しにしてobserveさせない
    - submitListとonBindViewHolderで制御する
  - TwoWayは別途仕組みが必要
- onClickListenerなどイベントはxmlに書かない
  - inflateのalsoでit.button.setOnClickListener
  - ここでviewModel.onClick(activityViewModel)などでVMに切り替える

# View Model

- Viewプロパティなどの要素は使わず抽象化する
- 全部がMutableLiveDataではない
- DB表示はLiveData
  - LiveDataを返すDAOはsuspendにはならない
  - nullを通知後結果を通知するようになる
- ObservableListは使わずにListAdapterで実現する
  - 見た目だけソートはソートキーとDBのLiveDataからMediatorLiveDataを作る
