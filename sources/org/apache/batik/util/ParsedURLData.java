/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.zip.GZIPInputStream;

public class ParsedURLData {

    /**
     * GZIP header magic number bytes, like found in a gzipped
     * files, which are encoded in Intel format (ie. little indian).
     */
    public final static byte GZIP_MAGIC[] = {(byte)0x1f, (byte)0x8b};

    /**
     * This is a utility function others can call that checks if
     * is is a GZIP stream if so it returns a GZIPInputStream that
     * will decode the contents, otherwise it returns (or a
     * buffered version of is) untouched.
     * @param is Stream that may potentially be a GZIP stream.
     */
    public static InputStream checkGZIP(InputStream is) 
        throws IOException {

            if (!is.markSupported())
                is = new BufferedInputStream(is);
            byte data[] = new byte[2];
            try {
                is.mark(2);
                is.read(data);
                is.reset();
            } catch (Throwable t) {
                is.reset();
                return is;
            }
        if ((data[0] == GZIP_MAGIC[0]) &&
            (data[1] == GZIP_MAGIC[1]))
            return new GZIPInputStream(is);
        return is;
    }

    /**
     * Since the Data instance is 'hidden' in the ParsedURL
     * instance we make all our methods public.  This makes it
     * easy for the various Protocol Handlers to update an
     * instance as parsing proceeds.
     */
    public String protocol = null;
    public String host     = null;
    public int    port     = -1;
    public String path     = null;
    public String ref      = null;

    /**
     * Void constructor
     */
    public ParsedURLData() {
    }

    /**
     * Build from an existing URL.
     */
    public ParsedURLData(URL url) {
        protocol = url.getProtocol();
        if ((protocol != null) && (protocol.length() == 0)) 
            protocol = null;

        host = url.getHost();
        if ((host != null) && (host.length() == 0)) 
            host = null;

        port     = url.getPort();

        path     = url.getPath();
        if ((path != null) && (path.length() == 0)) 
            path = null;

        ref      = url.getRef();
        if ((ref != null) && (ref.length() == 0))  
            ref = null;
    }

    /**
     * Attempts to build a normal java.net.URL instance from this
     * URL.
     */
    protected URL buildURL() throws MalformedURLException {
        String file = "";
        if (path != null) 
            file = path;
        if (ref != null)
            file += "#" + ref;

        if (port == -1)
            return new URL(protocol, host, file);

        return new URL(protocol, host, port, file);
    }

    /**
     * Implement Object.hashCode.
     */
    public int hashCode() {
        int hc = port;
        if (protocol != null) 
            hc ^= protocol.hashCode();
        if (host != null)
            hc ^= host.hashCode();

        // For some URLS path and ref can get fairly long
        // and the most unique part is towards the end
        // so we grab that part for HC purposes
        if (path != null) {
            int len = path.length();
            if (len > 20)
                hc ^= path.substring(len-20).hashCode();
            else
                hc ^= path.hashCode();
        }
        if (ref != null) {
            int len = ref.length();
            if (len > 20)
                hc ^= ref.substring(len-20).hashCode();
            else
                hc ^= ref.hashCode();
        }

        return hc;
    }

    /**
     * Implement Object.equals for ParsedURLData.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (! (obj instanceof ParsedURLData)) 
            return false;

        ParsedURLData ud = (ParsedURLData)obj;
        if (ud.port != port)
            return false;
            
        if (ud.protocol==null) {
            if (protocol != null)
                return false;
        } else if (protocol == null)
            return false;
        else if (!ud.protocol.equals(protocol))
            return false;

        if (ud.host==null) {
            if (host   !=null)
                return false;
        } else if (host == null)
            return false;
        else if (!ud.host.equals(host))
            return false;

        if (ud.ref==null) {
            if (ref   !=null)
                return false;
        } else if (ref == null)
            return false;
        else if (!ud.ref.equals(ref))
            return false;

        if (ud.path==null) {
            if (path   !=null)
                return false;
        } else if (path == null)
            return false;
        else if (!ud.path.equals(path))
            return false;

        return true;
    }

    /**
     * Returns true if the URL looks well formed and complete.
     * This does not garuntee that the stream can be opened but
     * is a good indication that things aren't totally messed up.
     */
    public boolean complete() {
        try {
            URL url = buildURL();
        } catch (MalformedURLException mue) {
            return false;
        }
        return true;
    }

    /**
     * Open the stream and check for common compression types.  If
     * the stream is found to be compressed with a standard
     * compression type it is automatically decompressed.
     */
    public InputStream openStream() throws IOException {
        InputStream raw = openStreamRaw();
        if (raw == null)
            return null;
                
        return checkGZIP(raw);
    }

    /**
     * Open the stream and returns it.  No checks are made to see
     * if the stream is compressed or encoded in any way.
     */
    public InputStream openStreamRaw() throws IOException {
        URL url = null;
        try {
            url = buildURL();
        } catch (MalformedURLException mue) {
            throw new IOException
                ("Unable to make sense of URL for connection");
        }

        if (url == null)
            return null;
        return url.openStream();
    }

    /**
     * Returns the URL up to and include the port number on
     * the host.  Does not include the path or fragment pieces.
     */
    public String getPortStr() {
        String portStr ="";
        if (protocol != null)
            portStr += protocol + ":";

        if ((host != null) || (port != -1)) {
            portStr += "//";
            if (host != null) portStr += host;
            if (port != -1)   portStr += ":" + port;
        }

        return portStr;
    }

    /**
     * Return a string representation of the data.
     */
    public String toString() {
        String ret = getPortStr();
        if (path != null)
            ret += path;

        if (ref != null) 
            ret += "#" + ref;

        return ret;
    }
}
