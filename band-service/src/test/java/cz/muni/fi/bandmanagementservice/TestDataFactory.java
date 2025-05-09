package cz.muni.fi.bandmanagementservice;

import cz.muni.fi.bandmanagementservice.dto.BandDto;
import cz.muni.fi.bandmanagementservice.dto.BandOfferDto;
import cz.muni.fi.bandmanagementservice.model.Band;
import cz.muni.fi.bandmanagementservice.model.BandOffer;

import java.util.Set;

public class TestDataFactory {

    public static Band setUpBand1() {
        Band band = new Band();
        band.setId(1L);
        band.setName("Band 1");
        band.setMusicalStyle("Rock");
        band.setManagerId(1L);
        band.setLogoUrl("http://example.com/logo1.png");
        band.setMembers(Set.of(1L, 2L, 3L));
        return band;
    }

    public static BandDto setUpBandDto1() {
        BandDto bandDto = new BandDto();
        bandDto.setId(1L);
        bandDto.setName("Band 1");
        bandDto.setMusicalStyle("Rock");
        bandDto.setLogo("http://example.com/logo1.png");
        bandDto.setManagerId(1L);
        bandDto.setMembers(Set.of(1L, 2L, 3L));
        return bandDto;
    }

    public static Band setUpBand2() {
        Band band = new Band();
        band.setId(2L);
        band.setName("Band 2");
        band.setMusicalStyle("Pop");
        band.setManagerId(2L);
        band.setLogoUrl("http://example.com/logo2.png");
        band.setMembers(Set.of(4L, 5L, 6L));
        return band;
    }

    public static BandDto setUpBandDto2() {
        BandDto bandDto = new BandDto();
        bandDto.setId(2L);
        bandDto.setName("Band 2");
        bandDto.setMusicalStyle("Pop");
        bandDto.setLogo("http://example.com/logo2.png");
        bandDto.setManagerId(2L);
        bandDto.setMembers(Set.of(4L, 5L, 6L));
        return bandDto;
    }



    public static BandOffer setUpBandOffer1() {
        BandOffer bandOffer = new BandOffer();
        bandOffer.setId(1L);
        bandOffer.setBandId(1L);
        bandOffer.setInvitedMusicianId(1L);
        bandOffer.setOfferingManagerId(1L);
        return bandOffer;
    }

    public static BandOfferDto setUpBandOfferDto1() {
        BandOfferDto bandOfferDto = new BandOfferDto();
        bandOfferDto.setId(1L);
        bandOfferDto.setBandId(1L);
        bandOfferDto.setInvitedMusicianId(1L);
        bandOfferDto.setOfferingManagerId(1L);
        bandOfferDto.setStatus(BandOfferDto.StatusEnum.PENDING);
        return bandOfferDto;
    }

    public static BandOffer setUpBandOffer2() {
        BandOffer bandOffer = new BandOffer();
        bandOffer.setId(2L);
        bandOffer.setBandId(2L);
        bandOffer.setInvitedMusicianId(2L);
        bandOffer.setOfferingManagerId(2L);
        return bandOffer;
    }

    public static BandOfferDto setUpBandOfferDto2() {
        BandOfferDto bandOfferDto = new BandOfferDto();
        bandOfferDto.setId(2L);
        bandOfferDto.setBandId(2L);
        bandOfferDto.setInvitedMusicianId(2L);
        bandOfferDto.setOfferingManagerId(2L);
        bandOfferDto.setStatus(BandOfferDto.StatusEnum.PENDING);
        return bandOfferDto;
    }
}
