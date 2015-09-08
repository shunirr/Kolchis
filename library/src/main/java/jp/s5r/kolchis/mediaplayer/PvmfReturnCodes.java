package jp.s5r.kolchis.mediaplayer;

// Unknown error code for MediaPlayer.
// http://stackoverflow.com/questions/13046753/complete-list-of-mediaplayer-error-codes
// https://github.com/android/platform_external_opencore/blob/master/pvmi/pvmf/include/pvmf_return_codes.h

public enum PvmfReturnCodes {
    PVMFSuccess(1),
    PVMFPending(0),
    PVMFNotSet(2),
    PVMFErrFirst(-1),
    PVMFFailure(-1),
    PVMFErrCancelled(-2),
    PVMFErrNoMemory(-3),
    PVMFErrNotSupported(-4),
    PVMFErrArgument(-5),
    PVMFErrBadHandle(-6),
    PVMFErrAlreadyExists(-7),
    PVMFErrBusy(-8),
    PVMFErrNotReady(-9),
    PVMFErrCorrupt(-10),
    PVMFErrTimeout(-11),
    PVMFErrOverflow(-12),
    PVMFErrUnderflow(-13),
    PVMFErrInvalidState(-14),
    PVMFErrNoResources(-15),
    PVMFErrResourceConfiguration(-16),
    PVMFErrResource(-17),
    PVMFErrProcessing(-18),
    PVMFErrPortProcessing(-19),
    PVMFErrAccessDenied(-20),
    PVMFErrLicenseRequired(-21),
    PVMFErrLicenseRequiredPreviewAvailable(-22),
    PVMFErrContentTooLarge(-23),
    PVMFErrMaxReached(-24),
    PVMFLowDiskSpace(-25),
    PVMFErrHTTPAuthenticationRequired(-26),
    PVMFErrCallbackHasBecomeInvalid(-27),
    PVMFErrCallbackClockStopped(-28),
    PVMFErrReleaseMetadataValueNotDone(-29),
    PVMFErrRedirect(-30),
    PVMFErrNotImplemented(-31),
    PVMFErrContentInvalidForProgressivePlayback(-32),
    PVMFErrLast(-100),
    PVMFInfoFirst(10),
    PVMFInfoPortCreated(10),
    PVMFInfoPortDeleted(11),
    PVMFInfoPortConnected(12),
    PVMFInfoPortDisconnected(13),
    PVMFInfoOverflow(14),
    PVMFInfoUnderflow(15),
    PVMFInfoProcessingFailure(16),
    PVMFInfoEndOfData(17),
    PVMFInfoBufferCreated(18),
    PVMFInfoBufferingStart(19),
    PVMFInfoBufferingStatus(20),
    PVMFInfoBufferingComplete(21),
    PVMFInfoDataReady(22),
    PVMFInfoPositionStatus(23),
    PVMFInfoStateChanged(24),
    PVMFInfoDataDiscarded(25),
    PVMFInfoErrorHandlingStart(26),
    PVMFInfoErrorHandlingComplete(27),
    PVMFInfoRemoteSourceNotification(28),
    PVMFInfoLicenseAcquisitionStarted(29),
    PVMFInfoContentLength(30),
    PVMFInfoContentTruncated(31),
    PVMFInfoSourceFormatNotSupported(32),
    PVMFInfoPlayListClipTransition(33),
    PVMFInfoContentType(34),
    PVMFInfoTrackDisable(35),
    PVMFInfoUnexpectedData(36),
    PVMFInfoSessionDisconnect(37),
    PVMFInfoStartOfData(38),
    PVMFInfoReportObserverRecieved(39),
    PVMFInfoMetadataAvailable(40),
    PVMFInfoDurationAvailable(41),
    PVMFInfoChangePlaybackPositionNotSupported(42),
    PVMFInfoPoorlyInterleavedContent(43),
    PVMFInfoActualPlaybackPosition(44),
    PVMFInfoLiveBufferEmpty(45),
    PVMFInfoPlayListSwitch(46),
    PVMFMIOConfigurationComplete(47),
    PVMFInfoVideoTrackFallingBehind(48),
    PVMFInfoSourceOverflow(49),
    PVMFInfoShoutcastMediaDataLength(50),
    PVMFInfoShoutcastClipBitrate(51),
    PVMFInfoIsShoutcastSesssion(52),
    PVMFInfoLast(100);

    private int code;

    PvmfReturnCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PvmfReturnCodes valueOf(int code) {
        for (PvmfReturnCodes c : values()) {
            if (c.code == code) {
                return c;
            }
        }
        return null;
    }
}
