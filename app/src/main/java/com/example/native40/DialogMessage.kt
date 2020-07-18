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
     * リクエストコード. 必須.
     */
    val requestCode: RequestCode,
    /**
     * タイトルリソースID. 任意.
     */
    val title: Int? = null,
    /**
     * メッセージリソースID. 任意.
     */
    val message: Int? = null,
    /**
     * 単一選択用リスト.
     */
    val items: List<String>? = null,
    /**
     * 例外.
     */
    val throwable: Throwable? = null
)