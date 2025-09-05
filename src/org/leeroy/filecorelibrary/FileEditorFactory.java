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

import static org.leeroy.filecorelibrary.smbj.SmbjUtils.isSMBjEnabled;
import static org.leeroy.filecorelibrary.sshj.SshjUtils.isSSHjEnabled;

import android.content.Context;
import android.net.Uri;

import org.leeroy.filecorelibrary.contentstorage.ContentStorageFileEditor;
import org.leeroy.filecorelibrary.ftp.FtpFileEditor;
import org.leeroy.filecorelibrary.jcifs.JcifsFileEditor;
import org.leeroy.filecorelibrary.localstorage.LocalStorageFileEditor;
import org.leeroy.filecorelibrary.sftp.SftpFileEditor;
import org.leeroy.filecorelibrary.smbj.SmbjFileEditor;
import org.leeroy.filecorelibrary.sshj.SshjFileEditor;
import org.leeroy.filecorelibrary.zip.ZipFileEditor;
import org.leeroy.filecorelibrary.webdav.WebdavFileEditor;

/**
 * create a file editor
 * @author alexandre
 *
 */
public class FileEditorFactory {
    public static FileEditor getFileEditorForUrl(Uri uri, Context ct) {
        if ("smb".equalsIgnoreCase(uri.getScheme())) {
            if (isSMBjEnabled()) return new SmbjFileEditor(uri);
            else return new JcifsFileEditor(uri);
        }
        else if ("ftp".equalsIgnoreCase(uri.getScheme())||"ftps".equalsIgnoreCase(uri.getScheme())) {
            return new FtpFileEditor(uri);
        }
        else if ("sftp".equalsIgnoreCase(uri.getScheme())) {
            if (isSSHjEnabled()) return new SshjFileEditor(uri);
            else return new SftpFileEditor(uri);
        }
        else if ("sshj".equalsIgnoreCase(uri.getScheme())) {
            return new SshjFileEditor(uri);
        }
        else if ("zip".equalsIgnoreCase(uri.getScheme())) {
            return new ZipFileEditor(uri);
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return new ContentStorageFileEditor(uri, ct);
        }
        else if ("webdav".equalsIgnoreCase(uri.getScheme())) {
            return new WebdavFileEditor(uri);
        }
        else if ("webdavs".equalsIgnoreCase(uri.getScheme())) {
            return new WebdavFileEditor(uri);
        }
        else if ("smbj".equalsIgnoreCase(uri.getScheme())) {
            return new SmbjFileEditor(uri);
        }
        else if (FileUtils.isLocal(uri)) {
            return new LocalStorageFileEditor(uri, ct);
        }
        else {
            throw new IllegalArgumentException("not implemented yet for "+uri);
        }
    }
}
