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

import android.net.Uri;

import org.leeroy.filecorelibrary.contentstorage.ContentFile2;
import org.leeroy.filecorelibrary.ftp.FTPFile2;
import org.leeroy.filecorelibrary.jcifs.JcifsFile2;
import org.leeroy.filecorelibrary.localstorage.JavaFile2;
import org.leeroy.filecorelibrary.sftp.SFTPFile2;
import org.leeroy.filecorelibrary.sftp.SftpFileEditor;
import org.leeroy.filecorelibrary.smbj.SmbjFile2;
import org.leeroy.filecorelibrary.sshj.SshjFile2;
import org.leeroy.filecorelibrary.sshj.SshjFileEditor;
import org.leeroy.filecorelibrary.webdav.WebdavFile2;

/**
 * Created by alexandre on 22/04/15.
 */
public class MetaFile2Factory {
    public static MetaFile2 getMetaFileForUrl(Uri uri) throws Exception {
        if ("smb".equalsIgnoreCase(uri.getScheme())) {
            if (isSMBjEnabled()) return SmbjFile2.fromUri(uri);
            else return JcifsFile2.fromUri(uri);
        }
        else if ("ftp".equalsIgnoreCase(uri.getScheme())||"ftps".equalsIgnoreCase(uri.getScheme())) {
            return FTPFile2.fromUri(uri);
        }
        else if ("sftp".equalsIgnoreCase(uri.getScheme())) {
            if (isSSHjEnabled()) return SshjFile2.fromUri(uri);
            else return SFTPFile2.fromUri(uri);
        }
        else if ("sshj".equalsIgnoreCase(uri.getScheme())) {
            return SshjFile2.fromUri(uri);
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return ContentFile2.fromUri(uri);
        }
        else if ("webdav".equalsIgnoreCase(uri.getScheme())) {
            return WebdavFile2.fromUri(uri);
        }
        else if ("webdavs".equalsIgnoreCase(uri.getScheme())) {
            return WebdavFile2.fromUri(uri);
        }
        else if ("smbj".equalsIgnoreCase(uri.getScheme())) {
            return SmbjFile2.fromUri(uri);
        }
        else if (FileUtils.isLocal(uri)) {
            return JavaFile2.fromUri(uri);
        }
        else {
            throw new IllegalArgumentException("not implemented yet for "+uri);
        }
    }

    public static int defaultPortForProtocol(String scheme) {
        switch(scheme) {
            case "smb": return 445;
            case "ftp": return 21;
            case "ftps": return 21;
            case "sftp": return 22;
            case "sshj": return 22;
            case "webdav": return 80;
            case "webdavs": return 443;
            case "smbj": return 445;
            default: throw new IllegalArgumentException("Invalid scheme " + scheme);
        }
    }
}
