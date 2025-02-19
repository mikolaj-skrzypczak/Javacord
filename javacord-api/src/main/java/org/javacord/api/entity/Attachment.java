package org.javacord.api.entity;

import org.javacord.api.entity.message.MessageFlag;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an attachment.
 */
public interface Attachment extends DiscordEntity {

    /**
     * Gets the file name of the attachment.
     *
     * @return The file name of the attachment.
     */
    String getFileName();

    /**
     * Gets the description of the attachment.
     *
     * @return The description of the attachment.
     */
    Optional<String> getDescription();

    /**
     * Gets the size of the attachment in bytes.
     *
     * @return The size of the attachment in bytes.
     */
    int getSize();

    /**
     * Gets the url of the attachment.
     *
     * @return The url of the attachment.
     */
    URL getUrl();

    /**
     * Gets the proxy url of the attachment.
     *
     * @return The proxy url of the attachment.
     */
    URL getProxyUrl();

    /**
     * Checks if the attachment is an image.
     *
     * @return Whether the attachment is an image or not.
     */
    default boolean isImage() {
        return getHeight().isPresent();
    }

    /**
     * Gets the height of the attachment, if it's an image.
     *
     * @return The height of the attachment.
     */
    Optional<Integer> getHeight();

    /**
     * Gets the width of the attachment, if it's an image.
     *
     * @return The width of the attachment.
     */
    Optional<Integer> getWidth();

    /**
     * Gets whether this attachment is ephemeral.
     * Ephemeral attachments will automatically be removed after a set period of time and
     * attachments on messages are guaranteed to be available as long as the message itself exists.
     *
     * @return True if the attachment is ephemeral.
     */
    Optional<Boolean> isEphemeral();

    /**
     * Gets the duration of the audio file in seconds
     * Only present if the message is of type {@link MessageFlag#IS_VOICE_MESSAGE}.
     *
     * @return The duration of the audio file in seconds.
     */
    Optional<Double> getDurationSeconds();

    /**
     * Gets the waveform data of this attachment.
     * Only present if the message is of type {@link MessageFlag#IS_VOICE_MESSAGE}.
     *
     * @return A byte array of the waveform of the audio file.
     */
    Optional<byte[]> getWaveForm();

    /**
     * Gets the base64 encoded string of the waveform of the audio file
     * Only present if the message is of type {@link MessageFlag#IS_VOICE_MESSAGE}.
     *
     * @return The base64 encoded string of the waveform of the audio file.
     */
    Optional<String> getWaveFormBase64();

    /**
     * Gets the flags of the attachment.
     *
     * @return The flags of the attachment.
     */
    Optional<AttachmentFlags> getFlags();

    /**
     * Downloads the attachment as an input stream.
     *
     * @return The attachment as an input stream.
     * @throws IOException If an IO error occurs.
     */
    InputStream asInputStream() throws IOException;

    /**
     * Downloads the attachment as a byte array.
     *
     * @return The attachment as a byte array.
     */
    CompletableFuture<byte[]> asByteArray();

    /**
     * Downloads the attachment as an image.
     *
     * @return The attachment as an image. Only present, if the attachment is an image.
     * @throws IllegalStateException If the attachment is not an image.
     */
    CompletableFuture<BufferedImage> asImage();

    /**
     * Checks whether the attachment is marked as a spoiler.
     *
     * <p>Discord encodes the information on whether a file is considered a spoiler in the file name. Any file whose
     * filename starts with {@code SPOILER_} is considered a spoiler.
     *
     * @return The spoiler status.
     */
    default boolean isSpoiler() {
        return getFileName().startsWith("SPOILER_");
    }

}
