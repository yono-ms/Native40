/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

/**
 * アラートダイアログ情報.
 * 組み合わせ:
 * 1. title/message
 * 2. title/items
 * 3. throwable
 */
data class DialogMessage(
    /**
     * リクエストキー.
     */
    val requestKey: RequestKey,
    /**
     * タイトルリソースID.
     */
    val title: Int? = null,
    /**
     * メッセージリソースID.
     */
    val message: Int? = null,
    /**
     * 可変メッセージID用の埋め込み文字列リスト.
     */
    val messageArgs: List<String>? = null,
    /**
     * 単一選択用リスト.
     */
    val items: List<String>? = null,
    /**
     * 例外.
     */
    val throwable: Throwable? = null
)