package group.one.dronefeeder.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.one.dronefeeder.dto.DeliveryCreateDto;
import group.one.dronefeeder.dto.DeliveryPatchDto;
import group.one.dronefeeder.dto.DeliveryUpdateDto;
import group.one.dronefeeder.exception.NotFoundException;
import group.one.dronefeeder.model.Delivery;
import group.one.dronefeeder.model.Drone;
import group.one.dronefeeder.model.Video;
import group.one.dronefeeder.repository.DeliveryRepository;
import group.one.dronefeeder.repository.DroneRepository;
import group.one.dronefeeder.repository.VideoRepository;

/**
 * Delivery Service.
 */
@Service
public class DeliveryService {
  @Autowired
  DeliveryRepository repository;

  @Autowired
  DroneRepository droneJpaRepository;

  @Autowired
  VideoRepository videoRepository;

  public List<Delivery> findAll() {
    return repository.findAll();
  }

  public Delivery findOne(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("Delivery Not Found!"));
  }

  public Delivery create(DeliveryCreateDto delivery) {
    Drone drone = droneJpaRepository.findById(delivery.getDrone())
        .orElseThrow(() -> new NotFoundException("Não encontrado id"));
    Delivery newDelivery = new Delivery(delivery.getLatitude(), delivery.getLongitude(), drone);
    return repository.save(newDelivery);
  }


  public Delivery update(Long id, DeliveryUpdateDto data) {

    Delivery delivery =
        repository.findById(id).orElseThrow(() -> new NotFoundException("Delivery Not Found!"));
    delivery.setLatitude(data.getLatitude());
    delivery.setLongitude(data.getLongitude());

    return repository.save(delivery);
  }


  public Delivery patch(Long id, DeliveryPatchDto data) {
    Delivery delivery =
        repository.findById(id).orElseThrow(() -> new NotFoundException("Delivery Not Found!"));
    Date date = new Date();
    Video video = videoRepository.findById(data.getVideo())
        .orElseThrow(() -> new NotFoundException("Delivery Not Found!"));

    delivery.setDeliveryStatus(true);
    delivery.setDeliveryDateAndTime(date);
    delivery.setVideo(video);
    return repository.save(delivery);
  }

  public void delete(Long id) {

    Delivery delivery =
        repository.findById(id).orElseThrow(() -> new NotFoundException("Delivery Not Found!"));

    repository.deleteById(delivery.getId());
  }

}
