package org.javacord.core.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.AttachmentFlags;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AttachmentImpl implements Attachment {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AttachmentImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The id of the attachment.
     */
    private final long id;

    /**
     * The file name of the attachment.
     */
    private final String fileName;

    /**
     * The description of the attachment.
     */
    private final String description;

    /**
     * The size of the attachment in bytes.
     */
    private final int size;

    /**
     * The url of the attachment.
     */
    private final String url;

    /**
     * The proxy url of the attachment.
     */
    private final String proxyUrl;

    /**
     * The height of the attachment if it's an image.
     */
    private final Integer height;

    /**
     * The width of the attachment if it's an image.
     */
    private final Integer width;

    /**
     * Whether this attachment is ephemeral.
     */
    private final Boolean ephemeral;

    /**
     * The duration of the audio file.
     */
    private final Double durationSeconds;

    /**
     * The base64 encoded bytearray representing a sampled waveform.
     */
    private final String waveform;

    /**
     * Attachment flags combined as a bitfield.
     */
    private final AttachmentFlags flags;

    /**
     * Creates a new attachment.
     *
     * @param api  The discord api instance.
     * @param data The data of the attachment.
     */
    public AttachmentImpl(DiscordApi api, final JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        fileName = data.get("filename").asText();
        description = data.hasNonNull("description") ? data.get("description").asText() : null;
        size = data.get("size").asInt();
        url = data.get("url").asText();
        proxyUrl = data.get("proxy_url").asText();
        height = data.hasNonNull("height") ? data.get("height").asInt() : null;
        width = data.hasNonNull("width") ? data.get("width").asInt() : null;
        ephemeral = data.hasNonNull("ephemeral") ? data.get("ephemeral").asBoolean() : null;
        durationSeconds = data.hasNonNull("duration_secs") ? data.get("duration_secs").doubleValue() : null;
        waveform = data.hasNonNull("waveform") ? data.get("waveform").asText() : null;
        Integer bitfield = data.hasNonNull("flags") ? data.get("flags").asInt() : null;
        flags = bitfield != null ? AttachmentFlags.getFlagTypeByBitfield(bitfield) : null;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the attachment is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public URL getProxyUrl() {
        try {
            return new URL(proxyUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy url of the attachment is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }

    @Override
    public Optional<Boolean> isEphemeral() {
        return Optional.ofNullable(ephemeral);
    }

    @Override
    public Optional<Double> getDurationSeconds() {
        return Optional.ofNullable(durationSeconds);
    }

    @Override
    public Optional<byte[]> getWaveForm() {
        return Optional.ofNullable(waveform).map(Base64.getDecoder()::decode);
    }

    @Override
    public Optional<String> getWaveFormBase64() {
        return Optional.ofNullable(waveform);
    }

    @Override
    public Optional<AttachmentFlags> getFlags() {
        return Optional.ofNullable(flags);
    }

    @Override
    public InputStream asInputStream() throws IOException {
        return new FileContainer(getUrl()).asInputStream(getApi());
    }

    @Override
    public CompletableFuture<byte[]> asByteArray() {
        return new FileContainer(getUrl()).asByteArray(getApi());
    }

    @Override
    public CompletableFuture<BufferedImage> asImage() {
        return new FileContainer(getUrl()).asBufferedImage(getApi());
    }

    /**
     * Creates the json structure of this attachment.
     *
     * @return The object node with the json data.
     */
    public ObjectNode toJsonNode() {
        ObjectNode attachments = JsonNodeFactory.instance.objectNode();

        attachments.put("id", id);
        attachments.put("filename", fileName);

        if (description != null) {
            attachments.put("description", description);
        }

        attachments.put("size", size);
        attachments.put("url", url);
        attachments.put("proxy_url", proxyUrl);

        if (height != null) {
            attachments.put("height", height);
        }

        if (width != null) {
            attachments.put("width", width);
        }

        if (ephemeral != null) {
            attachments.put("ephemeral", ephemeral);
        }

        return attachments;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
                || !((o == null)
                || (getClass() != o.getClass())
                || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Attachment (file name: %s, url: %s)", getFileName(), getUrl().toString());
    }
}
