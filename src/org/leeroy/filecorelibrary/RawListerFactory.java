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

import org.leeroy.filecorelibrary.contentstorage.ContentStorageRawLister;
import org.leeroy.filecorelibrary.ftp.FTPRawLister;
import org.leeroy.filecorelibrary.jcifs.JcifsFile2;
import org.leeroy.filecorelibrary.jcifs.JcifsRawLister;
import org.leeroy.filecorelibrary.localstorage.LocalStorageRawLister;
import org.leeroy.filecorelibrary.sftp.SFTPFile2;
import org.leeroy.filecorelibrary.sftp.SFTPRawLister;
import org.leeroy.filecorelibrary.smbj.SmbjFile2;
import org.leeroy.filecorelibrary.sshj.SshjFile2;
import org.leeroy.filecorelibrary.sshj.SshjRawLister;
import org.leeroy.filecorelibrary.webdav.WebdavRawLister;
import org.leeroy.filecorelibrary.smbj.SmbjRawLister;
import org.leeroy.filecorelibrary.zip.ZipRawLister;

public class RawListerFactory {

    public static RawLister getRawListerForUrl(Uri uri) {

        if ("smb".equals(uri.getScheme())) {
            if (isSMBjEnabled()) return new SmbjRawLister(uri);
            else return new JcifsRawLister(uri);
        }
        else if ("ftp".equals(uri.getScheme()) || "ftps".equals(uri.getScheme())) {
            return new FTPRawLister(uri) {
            };
        }
        else if ("sftp".equals(uri.getScheme())) {
            if (isSSHjEnabled()) return new SshjRawLister(uri);
            else return new SFTPRawLister(uri);
        }
        else if ("sshj".equals(uri.getScheme())) {
            return new SshjRawLister(uri);
        }
        else if ("zip".equals(uri.getScheme())) {
            return new ZipRawLister(uri);
        }
        else if ("content".equals(uri.getScheme())) {
            return new ContentStorageRawLister(uri);
        }
        else if (FileUtils.isLocal(uri)) {
            return new LocalStorageRawLister(uri);
        }
        else if("webdav".equals(uri.getScheme())) {
            return new WebdavRawLister(uri);
        }
        else if("webdavs".equals(uri.getScheme())) {
            return new WebdavRawLister(uri);
        }
        else if("smbj".equals(uri.getScheme())) {
            return new SmbjRawLister(uri);
        }
        else {
            throw new IllegalArgumentException("not implemented yet for "+uri);
        }
    }
}
