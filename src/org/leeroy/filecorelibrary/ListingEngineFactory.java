// Copyright 2017 LeeroyFlix
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.leeroy.filecorelibrary;

import android.content.Context;
import android.net.Uri;

import org.leeroy.filecorelibrary.contentstorage.ContentProviderListingEngine;
import org.leeroy.filecorelibrary.ftp.FtpListingEngine;
import org.leeroy.filecorelibrary.jcifs.JcifListingEngine;
import org.leeroy.filecorelibrary.localstorage.LocalStorageListingEngine;
import org.leeroy.filecorelibrary.sftp.SFtpListingEngine;
import org.leeroy.filecorelibrary.smbj.SmbjListingEngine;
import org.leeroy.filecorelibrary.sshj.SshjListingEngine;
import org.leeroy.filecorelibrary.webdav.WebdavListingEngine;
import org.leeroy.filecorelibrary.zip.ZipListingEngine;

public class ListingEngineFactory {
    public static ListingEngine getListingEngineForUrl(Context context, Uri uri) {
        if (FileUtils.isLocal(uri)&&(uri.getScheme()==null||!uri.getScheme().equals("content"))) {
            return new LocalStorageListingEngine(context, uri);
        }else if(uri.getScheme().equals("content")){
            return new ContentProviderListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("smb")) {
            return new JcifListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("ftp")||uri.getScheme().equals("ftps")) {
            return new FtpListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("sftp")) {
            return new SFtpListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("sshj")) {
            return new SshjListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("webdav")||uri.getScheme().equals("webdavs")) {
            return new WebdavListingEngine(context, uri);
        }
        // use jcifs-ng if no smbshare is provided i.e. uri is "/"
        else if (uri.getScheme().equals("smbj")) {
            if (uri.getPath().equals("/")) return new JcifListingEngine(context, uri);
            else return new SmbjListingEngine(context, uri);
        }
        else if (uri.getScheme().equals("zip")) {
            return new ZipListingEngine(context, uri);
        }
        else {
            try {
                return new GenericListingEngine(context, uri);
            } catch(Exception e) {
                throw new IllegalArgumentException("not implemented yet for " + uri, e);
            }
        }
    }

    public static class GenericListingEngine extends ListingEngine {
        final private Thread mListingThread = new Thread() {
            public void run() {
                try {
                    final var files = mRawLister.getFileList();
                    if(files == null) throw new Exception();
                    mUiHandler.post(() -> {
                        if (mListener != null) { // do not report error if aborted
                            if (!mAbort) {
                                mListener.onListingUpdate(files);
                            }
                            mListener.onListingEnd();
                        }
                    });
                } catch(AuthenticationException e) {
                    mUiHandler.post(() -> {
                        if (!mAbort && mListener != null) {
                            mListener.onListingFatalError(null, ErrorEnum.ERROR_AUTHENTICATION);
                        }
                    });
                } catch (Throwable t) {
                    mUiHandler.post(() -> {
                        if (!mAbort && mListener != null) { // do not report error if aborted
                            mListener.onListingFatalError(null, ErrorEnum.ERROR_UNKNOWN);
                        }
                    });
                }
            }
        };
        final private RawLister mRawLister;
        private boolean mAbort = false;

        public GenericListingEngine(Context context, Uri uri) {
            super(context);

            mRawLister = RawListerFactory.getRawListerForUrl(uri);
        }

        @Override
        public void start() {
            // Tell ASAP the listener that we are starting discovery
            mUiHandler.post(() -> {
                if (mListener != null) {
                    mListener.onListingStart();
                }
            });

            mListingThread.start();
        }

        @Override
        public void abort() {
            mAbort = true;
        }

    }
}
