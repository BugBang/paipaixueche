package com.session.common.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class UriUtil {

	/**
	 * Get the absolute path of uri
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @return the absolute path of uri
	 */
	public static String getPath(final Context context, final Uri uri) {
		if (needNewApi(context, uri)) {// DocumentProvider
			return getPathNew(context, uri);
		} else {
			return getPathOld(context, uri);
		}
	}

	/**
	 * Get the absolute path of uri，android sdk lower than kitkat
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @return the absolute path of uri
	 */
	public static String getPathOld(final Context context, final Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {// TrainingFile
			return uri.getPath();
		}
		return null;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static boolean needNewApi(final Context context, final Uri uri) {
		boolean res = false;
		final boolean isKitKatOrNew = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		res = isKitKatOrNew && DocumentsContract.isDocumentUri(context, uri);
		return res;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPathNew(final Context context, final Uri uri) {
		// ExternalStorageProvider
		if (isExternalStorageDocument(uri)) {
			final String docId = DocumentsContract.getDocumentId(uri);
			final String[] split = docId.split(":");
			final String type = split[0];

			if ("primary".equalsIgnoreCase(type)) {
				return Environment.getExternalStorageDirectory() + "/" + split[1];
			}

		}
		// DownloadsProvider
		else if (isDownloadsDocument(uri)) {

			final String id = DocumentsContract.getDocumentId(uri);
			final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

			return getDataColumn(context, contentUri, null, null);
		}
		// MediaProvider
		else if (isMediaDocument(uri)) {
			final String docId = DocumentsContract.getDocumentId(uri);
			final String[] split = docId.split(":");
			final String type = split[0];

			Uri contentUri = null;
			if ("image".equals(type)) {
				contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			} else if ("video".equals(type)) {
				contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
			} else if ("audio".equals(type)) {
				contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			}

			final String selection = "_id=?";
			final String[] selectionArgs = new String[] { split[1] };

			return getDataColumn(context, contentUri, selection, selectionArgs);
		}
		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}
